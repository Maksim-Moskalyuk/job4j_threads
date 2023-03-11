package ru.job4j.concurrent;

public class Wget {

    public static void main(String[] args) {
        Thread thread = new Thread(
                () -> {
                    for (int index = 0; index <= 100; index++) {
                        try {
                            Thread.sleep(1000);
                            System.out.print("\rLoading : " + index  + "%");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(System.lineSeparator() + "Загрузка завершена");
                }
        );
        thread.start();
    }

}
