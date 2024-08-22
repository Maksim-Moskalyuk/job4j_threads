package ru.job4j.concurrent.resource.ref;

public final class User {
    private final int id;
    private final String name;

    private User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static User of(String name) {
        return new User(0, name);
    }

    public static User of(int id, String name) {
        return new User(id, name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User withId(int id) {
        return new User(id, this.name);
    }

    public User withName(String name) {
        return new User(this.id, name);
    }

}
