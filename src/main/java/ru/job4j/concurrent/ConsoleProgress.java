package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        var process = new char[] {'-', '\\', '|', '/'};
        int index = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print("\r Loading ... " + process[index]);
            index = (index + 1) % process.length;
            try {
                Thread.sleep(5000); /* симулируем выполнение параллельной задачи в течение 5 секунд. */
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        progress.interrupt();
    }
}
