module adventofcode.shared {
    exports org.adb.adventofcode;
    exports org.adb.adventofcode.io;

    requires transitive info.picocli;

    opens org.adb.adventofcode to info.picocli;
}