package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

                // Рассчитываем, сколько времени должно было пройти для соблюдения лимита скорости
                long expectedTime = (downloadedBytes * 1000L) / speed;  // время в миллисекундах
                long actualTime = (System.nanoTime() - startTime) / 1_000_000;  // реальное прошедшее время в миллисекундах

                // Если прошла секунда, выводим количество скачанных байт
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSecond >= 1000) {
                    System.out.println("Downloaded in the last second: " + downloadedBytesThisSecond + " bytes");
                    downloadedBytesThisSecond = 0;
                    lastSecond = currentTime;
                }

                if (actualTime < expectedTime) {
                    try {
                        Thread.sleep(expectedTime - actualTime);  // делаем паузу, если скачивание слишком быстрое
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();  // восстанавливаем статус прерывания потока
                    }
                }
            }

            // Выводим данные за последнюю неполную секунду, если остались байты
            if (downloadedBytesThisSecond > 0) {
                System.out.println("Downloaded in the last second: " + downloadedBytesThisSecond + " bytes");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes total");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
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
