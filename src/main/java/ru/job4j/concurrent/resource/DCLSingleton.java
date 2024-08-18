package ru.job4j.concurrent.resource;

public final class DCLSingleton {

    /*
     Переменная должна быть volatile, чтобы изменения в переменной instance были сразу видны всем потокам
     */
    private static volatile DCLSingleton instance;

    public static DCLSingleton getInstance() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }

    private DCLSingleton() {
    }

}
