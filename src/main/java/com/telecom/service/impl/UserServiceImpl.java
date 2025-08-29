package com.telecom.service.impl;

import com.telecom.exceptions.CustomerNotFoundException;
import com.telecom.exceptions.UnauthorizedException;
import com.telecom.exceptions.UserNotFoundException;
import com.telecom.models.User;
import com.telecom.repository.interfaces.UserRepo;
import com.telecom.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    public void createUser(User user) {
        userRepo.createUser(user);
    }

    @Override
    public User authenticateUser(String username, String password) throws UnauthorizedException, UserNotFoundException {
        Optional<User> user = userRepo.getUserByUserName(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with username: " + username + " not found");
        }
        return user.filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

    }

    @Override
    public User getUserByCustomerId(String customerId) throws CustomerNotFoundException {
        Optional<User> user = userRepo.getUserByCustomerId(customerId);
        if (user.isEmpty())
            throw new CustomerNotFoundException("Customer with id: " + customerId + " not found");
        return user.get();
    }
}
