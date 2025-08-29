package com.telecom.presentation;

import com.telecom.exceptions.UnauthorizedException;
import com.telecom.exceptions.UserNotFoundException;
import com.telecom.models.User;
import com.telecom.repository.interfaces.UserRepo;
import com.telecom.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * Console viewer for each user-based operation
 */

@RequiredArgsConstructor
public class LoginController {
    private final Scanner sc;
    private final UserService userService;

    /**
     * Login CLI
     * @return User
     */
    public User login() {
        System.out.println("Login");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        User user = userService.authenticateUser(username, password);
        System.out.println("User Authenticated Successfully");
        return user;

    }
}
