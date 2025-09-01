package com.telecom;

import com.telecom.bootstrap.BootstrapEngine;
import com.telecom.models.User;
import com.telecom.models.enums.Role;
import com.telecom.presentation.*;
import com.telecom.repository.impl.*;
import com.telecom.repository.interfaces.*;
import com.telecom.service.impl.*;
import com.telecom.service.interfaces.*;

import java.util.Scanner;

/**
 * Main Project App - Starting point of the project
 */
public class MainApp {
    public static void main(String[] args) {
        // --- DEPENDENCY INITIALIZATION ---
        Scanner sc = new Scanner(System.in);

        // Repositories
        UserRepo userRepo = new UserRepoImpl();
        CustomerRepo customerRepo = new CustomerRepoImpl();
        PlanRepo planRepo = new PlanRepoImpl();
        SubscriptionRepo subscriptionRepo = new SubscriptionRepoImpl();
        UsageRecordRepo usageRecordRepo = new UsageRecordRepoImpl();
        InvoiceRepo invoiceRepo = new InvoiceRepoImpl();
        FamilyRepo familyRepo = new FamilyRepoImpl();

        // Bootstrap Engine
        BootstrapEngine bootstrapEngine = new BootstrapEngine(userRepo, customerRepo, planRepo, subscriptionRepo, usageRecordRepo);
        bootstrapEngine.run();

        // Services
        UserService userService = new UserServiceImpl(userRepo);
        CustomerService customerService = new CustomerServiceImpl(customerRepo);
        PlanService planService = new PlanServiceImpl(planRepo);
        FamilyService familyService = new FamilyServiceImpl(familyRepo);
        SubscriptionService subscriptionService = new SubscriptionServiceImpl(subscriptionRepo, familyService);
        BillingService billingService = new BillingServiceImpl(invoiceRepo);
        AnalyticsService analyticsService = new AnalyticsServiceImpl();

        // Controllers
        LoginController loginController = new LoginController(sc, userService);
        PlanController planController = new PlanController(sc, planService);
        AnalyticsController analyticsController = new AnalyticsController(sc, analyticsService, usageRecordRepo, invoiceRepo, planRepo);
        BillingController billingController = new BillingController(billingService, subscriptionService, planService, usageRecordRepo);
        AdminController adminController = new AdminController(sc, planService, customerService, userService, subscriptionService, planController, analyticsController);
        SubscriptionController subscriptionController = new SubscriptionController(sc, subscriptionService, planService);
        CustomerController customerController = new CustomerController(sc, subscriptionService, planService, usageRecordRepo, invoiceRepo, familyService, customerService, billingController, subscriptionController, billingService);
        // --- APPLICATION START ---
        System.out.println("\nWelcome to the Telecom Billing & Analytics System");

        while (true) {
            try {
                User user = loginController.login();
                if (user == null) {
                    System.out.println("Login failed. Exiting.");
                    return;
                }

                // --- ROLE-BASED MENU ---
                if (user.getRole() == Role.ADMIN) {
                    adminController.showAdminMenu();
                } else if (user.getRole() == Role.CUSTOMER) {
                    customerController.showCustomerMenu(user);
                }

                System.out.println("Logout successful. Goodbye!");

            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                // e.printStackTrace(); // Uncomment for debugging
            }
        }
    }
}
