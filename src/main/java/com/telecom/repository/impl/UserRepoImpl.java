package com.telecom.repository.impl;

import com.telecom.models.User;
import com.telecom.repository.interfaces.UserRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepoImpl implements UserRepo {
    private final Map<String, User> db = new HashMap<>();

    @Override
    public void createUser(User user) {
        db.put(user.getUsername(), user);

    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        if (db.containsKey(userName)) {
            return Optional.of(db.get(userName));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByCustomerId(String customerId) {
        for (User user : db.values()) {
            if (user.getCustomerId().equals(customerId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
