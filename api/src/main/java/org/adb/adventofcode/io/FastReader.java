package org.adb.adventofcode.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

abstract class FastReader implements Closeable {

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

    public IntStream asIntStream() {
        IntStream.Builder streamBuilder = IntStream.builder();
        boolean hasNext = true;
        while (hasNext) {
            try {
                streamBuilder.accept(nextInt());
            } catch (RuntimeException e) {
                hasNext = false;
            }
        }
        return streamBuilder.build();
    }

    public LongStream asLongStream() {
        LongStream.Builder streamBuilder = LongStream.builder();
        boolean hasNext = true;
        while (hasNext) {
            try {
                streamBuilder.accept(nextLong());
            } catch (RuntimeException e) {
                hasNext = false;
            }
        }
        return streamBuilder.build();
    }

    public DoubleStream asDoubleStream() {
        DoubleStream.Builder streamBuilder = DoubleStream.builder();
        boolean hasNext = true;
        while (hasNext) {
            try {
                streamBuilder.accept(nextDouble());
            } catch (RuntimeException e) {
                hasNext = false;
            }
        }
        return streamBuilder.build();
    }

    public <T> Stream<? super T> asStream(Function<String, ? extends T> converter) {
        Stream.Builder<T> streamBuilder = Stream.builder();
        boolean hasNext = true;
        while (hasNext) {
            try {
                T element = converter.apply(next());
                streamBuilder.accept(element);
            } catch (RuntimeException e) {
                hasNext = false;
            }
        }
        return streamBuilder.build();
    }

    private String next() {
        while (stringTokenizer == null || !stringTokenizer.hasMoreElements()) {
            try {
                stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            } catch (IOException e) {
                throw new RuntimeIoException(e);
            }
        }
        return stringTokenizer.nextToken();
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
