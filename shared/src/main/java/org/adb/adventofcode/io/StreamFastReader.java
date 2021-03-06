package org.adb.adventofcode.io;

import java.io.InputStream;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

abstract class StreamFastReader extends FastReader {

    protected StreamFastReader(InputStream inputStream) {
        super(inputStream);
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

    public <T> Stream<T> asStream(Function<String, ? extends T> converter) {
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

    public Stream<String> asLines() {
        Stream.Builder<String> streamBuilder = Stream.builder();
        boolean hasNext = true;
        while (hasNext) {
            String line = nextLine();
            if (line != null) {
                streamBuilder.accept(line);
            } else {
                hasNext = false;
            }
        }
        return streamBuilder.build();
    }

    public <T> Stream<T> parseLines(Function<String, ? extends T> converter) {
        return asLines().map(converter);
    }

    public Stream<String> asMultilines() {
        Stream.Builder<String> streamBuilder = Stream.builder();
        boolean hasNext = true;
        StringBuilder multiline = new StringBuilder();
        while (hasNext) {
            String line = nextLine();
            if (line != null) {
                if (!line.isEmpty()) {
                    multiline.append(line).append('\n');
                } else {
                    streamBuilder.accept(multiline.substring(0, multiline.length() - 1));
                    multiline.setLength(0);
                }
            } else {
                streamBuilder.accept(multiline.substring(0, multiline.length() - 1));
                hasNext = false;
            }
        }
        return streamBuilder.build();
    }

    public <T> Stream<T> parseMultilines(Function<String, ? extends T> converter) {
        return asMultilines().map(converter);
    }
}
