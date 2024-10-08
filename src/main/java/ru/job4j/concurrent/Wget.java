package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File(generateUniqueFileName(fileName(url)));
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");

            processDownload(input, output);

            System.out.println(Files.size(file.toPath()) + " bytes total");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    private void processDownload(InputStream input, FileOutputStream output) throws IOException {
        var dataBuffer = new byte[512];
        int bytesRead;
        long downloadedBytes = 0;
        long downloadedBytesThisSecond = 0;
        long startTime = System.nanoTime();
        long lastSecond = System.currentTimeMillis();

        while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
            output.write(dataBuffer, 0, bytesRead);
            downloadedBytes += bytesRead;
            downloadedBytesThisSecond += bytesRead;

            long expectedTime = (downloadedBytes * 1000L) / speed;
            long actualTime = (System.nanoTime() - startTime) / 1_000_000;

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSecond >= 1000) {
                System.out.println("Downloaded in the last second: " + downloadedBytesThisSecond + " bytes");
                downloadedBytesThisSecond = 0;
                lastSecond = currentTime;
            }

            if (actualTime < expectedTime) {
                try {
                    Thread.sleep(expectedTime - actualTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (downloadedBytesThisSecond > 0) {
            System.out.println("Downloaded in the last second: " + downloadedBytesThisSecond + " bytes");
        }
    }

    private String fileName(String urlPath) {
        var fileName = "file";
        try {
            var url = new URL(urlPath);
            var filePath = url.getPath();
            fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    private String generateUniqueFileName(String fileName) {
        var file = new File(fileName);
        var name = file.getName();
        var baseName = name;
        var extension = "";

        var dotIndex = name.lastIndexOf('.');
        if (dotIndex != -1) {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        int counter = 1;
        while (file.exists()) {
            file = new File(baseName + "_" + counter + extension);
            counter++;
        }

        return file.getName();
    }
}
