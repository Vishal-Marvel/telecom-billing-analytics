package com.telecom.service.interfaces;

import com.telecom.exceptions.CustomerNotFoundException;
import com.telecom.exceptions.UnauthorizedException;
import com.telecom.exceptions.UserNotFoundException;
import com.telecom.models.User;

import java.util.Optional;

public interface UserService {
    void createUser(User user);

    User authenticateUser(String username, String password) throws UnauthorizedException, UserNotFoundException;

    User getUserByCustomerId(String customerId) throws CustomerNotFoundException;

}
