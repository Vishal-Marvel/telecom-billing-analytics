package com.telecom;

import com.telecom.bootstrap.BootstrapEngine;
import com.telecom.presentation.LoginController;
import com.telecom.repository.impl.UserRepoImpl;
import com.telecom.repository.interfaces.UserRepo;
import com.telecom.service.impl.UserServiceImpl;
import com.telecom.service.interfaces.UserService;

import java.util.Scanner;

/**
 * Main Project App - Starting point of the project
 */
public class MainApp {
    public static void main(String[] args) {
        // Console input
        Scanner sc = new Scanner(System.in);
        // Repositories initialization
        UserRepo userRepo = new UserRepoImpl();
        // Bootstrap code
        BootstrapEngine bootstrapEngine = new BootstrapEngine(userRepo);
        bootstrapEngine.run();
        // Services initialization
        UserService userService = new UserServiceImpl(userRepo);
        // Controllers Initialization
        LoginController loginController = new LoginController(sc, userService);

        // Test

        try {
            loginController.login();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
