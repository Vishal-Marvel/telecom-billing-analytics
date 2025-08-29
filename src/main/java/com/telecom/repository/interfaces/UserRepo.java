package com.telecom.repository.interfaces;

import com.telecom.models.User;

import java.util.Optional;

public interface UserRepo {
    void createUser(User user);
    Optional<User> getUserByUserName(String userName);
    Optional<User> getUserByCustomerId(String customerId);
}
