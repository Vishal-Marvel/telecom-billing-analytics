package com.telecom.presentation;

import com.telecom.models.Customer;
import com.telecom.models.Plan;
import com.telecom.models.Subscription;
import com.telecom.models.User;
import com.telecom.models.enums.Role;
import com.telecom.service.interfaces.CustomerService;
import com.telecom.service.interfaces.PlanService;
import com.telecom.service.interfaces.SubscriptionService;
import com.telecom.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Console viewer for all Administrator-specific operations.
 */
@RequiredArgsConstructor
public class AdminController {
    private final Scanner sc;
    private final PlanService planService;
    private final CustomerService customerService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final PlanController planController;

    /**
     * Main menu for Admin users.
     */
    public void showAdminMenu() {
        while (true) {
            try {
                System.out.println("\n--- Admin Menu ---");
                System.out.println("1. Manage Plans");
                System.out.println("2. Create New Customer");
                System.out.println("3. View All Customers");
                System.out.println("4. View Customer Details");
                System.out.println("0. Logout");
                System.out.print("Select an option: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1: managePlans(); break;
                    case 2: createNewCustomer(); break;
                    case 3: viewAllCustomers(); break;
                    case 4: viewCustomerDetails(); break;
                    case 0: return;
                    default: System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void viewCustomerDetails() {
        System.out.println("\n--- View Customer Details ---");
        System.out.print("Enter customer ID: ");
        String customerId = sc.nextLine();

        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Customer Details:");
        System.out.printf("  ID: %s%n", customer.getId());
        System.out.printf("  Name: %s%n", customer.getName());
        System.out.printf("  Email: %s%n", customer.getEmail());

        List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(customerId);
        if (subscriptions.isEmpty()) {
            System.out.println("  No subscriptions found for this customer.");
        } else {
            System.out.println("  Subscriptions:");
            for (Subscription sub : subscriptions) {
                Plan plan = planService.getPlan(sub.getPlanId());
                System.out.printf("    - Subscription ID: %s%n", sub.getId());
                System.out.printf("      Phone Number: %s%n", sub.getPhoneNumber());
                System.out.printf("      Plan: %s (%.2f/month)%n", plan.getName(), plan.getMonthlyRental());
                System.out.printf("      Start Date: %s%n", sub.getStartDate());
            }
        }
    }

    private void viewAllCustomers() {
        System.out.println("\n--- All Customers ---");
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            System.out.printf("ID: %s, Name: %s, Email: %s%n",
                    customer.getId(), customer.getName(), customer.getEmail());
        }
    }

    /**
     * Menu for managing plans (Add, View, Update, Delete).
     */
    private void managePlans() {
        while (true) {
            try {
                System.out.println("\n--- Plan Management ---");
                System.out.println("1. View All Plans");
                System.out.println("2. Add New Plan");
                System.out.println("3. Update Plan");
                System.out.println("4. Delete Plan");
                System.out.println("0. Back to Admin Menu");
                System.out.print("Select an option: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1: planController.showAllPlans(); break;
                    case 2: planController.addNewPlan(); break;
                    case 3: planController.updateExistingPlan(); break;
                    case 4: planController.deleteExistingPlan(); break;
                    case 0: return;
                    default: System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * CLI to create a new customer and a corresponding user account.
     */
    private void createNewCustomer() {
        try {
            System.out.println("\n--- Create New Customer ---");
            System.out.print("Enter Customer ID: ");
            String customerId = sc.nextLine();
            System.out.print("Enter Customer Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Customer Email: ");
            String email = sc.nextLine();
            System.out.print("Enter Customer Referrer: ");
            String referrer = sc.nextLine();

            Customer newCustomer = new Customer(customerId, name, email, referrer.isEmpty() ? null : referrer, false);
            customerService.addCustomer(newCustomer);

            // Auto-generate a user account for the new customer
            String username = name.toLowerCase().replaceAll("\\s", "") + "_user";
            String password = "password123"; // Default password
            User newUser = User.builder()
                    .username(username)
                    .password(password)
                    .role(Role.CUSTOMER)
                    .customerId(customerId)
                    .build();
            userService.createUser(newUser);

            System.out.println("Customer created successfully!");
            System.out.println("--- New User Credentials ---");
            System.out.printf("  Username: %s%n", username);
            System.out.printf("  Password: %s%n", password);
            System.out.println("--------------------------");

            // Assign a plan to the new customer
            while (true) {
                try {
                    System.out.println("\n--- Assign a Plan to the New Customer ---");
                    planController.showAllPlans();
                    System.out.print("Enter the ID of the plan to assign: ");
                    String planId = sc.nextLine();
                    System.out.print("Enter phone number for the subscription: ");
                    String phoneNumber = sc.nextLine();

                    Subscription newSubscription = Subscription.builder()
                            .id("S" + (int) (Math.random() * 1000))
                            .customerId(customerId)
                            .phoneNumber(phoneNumber)
                            .planId(planId)
                            .startDate(LocalDate.now())
                            .build();
                    subscriptionService.addSubscription(newSubscription);

                    System.out.println("Subscription added successfully!");
                    break; // Exit the loop if successful
                } catch (Exception e) {
                    System.out.println("An error occurred while assigning the plan: " + e.getMessage());
                    System.out.println("Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while creating the customer: " + e.getMessage());
        }
    }
}
