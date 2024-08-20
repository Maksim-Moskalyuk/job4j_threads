package ru.job4j.concurrent.synchronize;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent(Predicate<Character> filter) throws IOException {
        String output = "";
        try (InputStream input = new FileInputStream(file)) {
            int data;
            while ((data = input.read()) > 0) {
                output += (char) data;
            }
        }
        return output;
    }

    public String getContent() throws IOException {
        return getContent(null);
    }

    public String getContentWithoutUnicode() throws IOException {
        return getContent(n -> n < 0x80);
    }

}
