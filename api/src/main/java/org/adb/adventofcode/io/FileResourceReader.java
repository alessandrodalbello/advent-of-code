package org.adb.adventofcode.io;

public class FileResourceReader extends StreamFastReader {

    public FileResourceReader(String filename) {
        super(FileResourceReader.class.getClassLoader().getResourceAsStream(filename));
    }
}
