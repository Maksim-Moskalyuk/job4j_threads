package ru.job4j.concurrent.resource.ref;

public class ShareNotSafe {
    public static void main(String[] args) throws InterruptedException {
        UserCache cache = new UserCache();
        User user = User.of("main");
        cache.add(user);

        Thread first = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cache.add(user.withName("first"));
            }
        });

        Thread second = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                cache.add(user.withName("second"));
            }
        });

        first.start();
        second.start();
        first.join();
        second.join();

        System.out.println(cache.findById(1).getName());
    }

}
