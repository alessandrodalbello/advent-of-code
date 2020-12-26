package org.adb.adventofcode.io;

import java.io.IOException;

public class RuntimeIoException extends RuntimeException {

    public RuntimeIoException(IOException ioException) {
        super(ioException);
    }
}
