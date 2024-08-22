package ru.job4j.concurrent.synchronize;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent(Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (InputStream input = new FileInputStream(file)) {
            int data;
            while ((data = input.read()) != -1) {
                char ch = (char) data;
                if (filter == null || filter.test(ch)) {
                    output.append(ch);
                }
            }
        }
        return output.toString();
    }

    public String getContent() throws IOException {
        return getContent(n -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return getContent(n -> n < 0x80);
    }

}
