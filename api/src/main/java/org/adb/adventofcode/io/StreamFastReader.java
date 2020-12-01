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
}
