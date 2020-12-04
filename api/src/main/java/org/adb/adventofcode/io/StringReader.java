package org.adb.adventofcode.io;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class StringReader extends FastReader {

    public StringReader(String string) {
        super(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
    }
}
