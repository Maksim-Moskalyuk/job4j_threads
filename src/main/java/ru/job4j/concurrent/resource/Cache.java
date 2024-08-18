package ru.job4j.concurrent.resource;

public final class Cache {
    private static Cache cache;

    public static Cache getInstance() {
        /*Двойная проверка, чтобы избежать ненужной синхронизации, когда объект уже создан*/
        if (cache == null) {
            synchronized (Cache.class) {
                if (cache == null) {
                    cache = new Cache();
                }
            }
        }
        return cache;
    }
}
