package ru.job4j.concurrent.resource.ref;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public void add(User user) {
        int userId = id.incrementAndGet();
        users.put(userId, user.withId(userId));
    }

    public User findById(int id) {
        User user = users.get(id);
        if (user != null) {
            return user;
        }
        return null;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
