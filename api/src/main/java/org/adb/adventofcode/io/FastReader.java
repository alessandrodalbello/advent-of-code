package org.adb.adventofcode.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public abstract class FastReader implements Closeable {

    private final BufferedReader bufferedReader;

    private StringTokenizer stringTokenizer;

    protected FastReader(InputStream inputStream) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public char nextChar() {
        return next().charAt(0);
    }

    public String nextString() {
        return next();
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

    public int[] nextIntArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = nextInt();
        }
        return array;
    }

    public long[] nextLongArray(int size) {
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = nextLong();
        }
        return array;
    }

    public double[] nextDoubleArray(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = nextDouble();
        }
        return array;
    }

    public char[] nextCharArray(int size) {
        char[] array = new char[size];
        for (int i = 0; i < size; i++) {
            array[i] = nextChar();
        }
        return array;
    }

    public String nextLine() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    protected String next() {
        while (stringTokenizer == null || !stringTokenizer.hasMoreElements()) {
            try {
                stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            } catch (IOException e) {
                throw new RuntimeIoException(e);
            }
        }
        return stringTokenizer.nextToken();
    }

    private String[] split(String regex) {
        return next().split(regex);
    }

    @Override
    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            // do nothing
        }
    }
}
