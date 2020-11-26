package org.adb.adventofcode.io;

public class FileResourceReader extends FastReader {

    public FileResourceReader(String filename) {
        super(FileResourceReader.class.getClassLoader().getResourceAsStream(filename));
    }
}
