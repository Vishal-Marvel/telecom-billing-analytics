package com.telecom.presentation;

import com.telecom.models.*;
import com.telecom.models.enums.Role;
import com.telecom.repository.interfaces.InvoiceRepo;
import com.telecom.repository.interfaces.UsageRecordRepo;
import com.telecom.service.interfaces.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Console viewer for all Customer-specific operations.
 */
@RequiredArgsConstructor
public class CustomerController {
    private final Scanner sc;
    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final UsageRecordRepo usageRecordRepo;
    private final InvoiceRepo invoiceRepo;
    private final BillingService billingService;
    private final FamilyService familyService;
    private final CustomerService customerService;
    private final UserService userService;

    /**
     * Main menu for Customer users.
     * @param user The currently authenticated user.
     */
    public void showCustomerMenu(User user) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View My Subscriptions");
            System.out.println("2. Add Usage (Simulate)");
            System.out.println("3. Change Plan");
            System.out.println("4. View Usage History");
            System.out.println("5. View My Bills");
            System.out.println("6. Pay a Bill");
            System.out.println("7. Add Family Member");
            System.out.println("0. Logout");
            System.out.print("Select an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: new SubscriptionController(sc, subscriptionService, planService).showCustomerSubscriptions(user); break;
                case 2: addUsage(user); break;
                case 3: changePlan(user); break;
                case 4: viewUsageHistory(user); break;
                case 5: viewMyBills(user); break;
                case 6: payBill(user); break;
                case 7: addFamilyMember(user); break;
                case 0: return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addFamilyMember(User currentUser) {
        System.out.println("\n--- Add a Family Member ---");

        // Find the current user's family subscription
        Subscription primarySub = subscriptionService.getCustomerSubscriptions(currentUser.getCustomerId())
                .stream().filter(s -> s.getFamilyId() != null).findFirst()
                .orElse(null);

        if (primarySub == null) {
            System.out.println("You do not have a family plan subscription.");
            return;
        }

        System.out.print("Enter new member's Customer ID: ");
        String customerId = sc.nextLine();
        System.out.print("Enter new member's Name: ");
        String name = sc.nextLine();
        System.out.print("Enter new member's Email: ");
        String email = sc.nextLine();
        System.out.print("Enter new member's Phone Number: ");
        String phone = sc.nextLine();

        // Create Customer and User
        Customer newCustomer = new Customer(customerId, name, email, null, false);
        customerService.addCustomer(newCustomer);
        User newUser = User.builder().username(name.toLowerCase() + "_user").password("password123").role(Role.CUSTOMER).customerId(customerId).build();
        userService.createUser(newUser);

        // Add to family
        familyService.addFamilyMember(primarySub.getFamilyId(), customerId);

        // Create subscription for the new member
        Subscription newSubscription = Subscription.builder()
                .id("S" + (int)(Math.random()*1000))
                .customerId(customerId)
                .phoneNumber(phone)
                .planId(primarySub.getPlanId()) // Same plan as primary
                .familyId(primarySub.getFamilyId())
                .startDate(LocalDate.now())
                .build();
        subscriptionService.addSubscription(newSubscription);

        System.out.println("Family member added successfully!");
        System.out.printf("New user credentials -> Username: %s, Password: %s%n", newUser.getUsername(), newUser.getPassword());
    }

    // other methods...
    private void addUsage(User user) { /* ... */ }
    private void changePlan(User user) { /* ... */ }
    private void viewUsageHistory(User user) { /* ... */ }
    private void viewMyBills(User user) { /* ... */ }
    private void payBill(User user) { /* ... */ }
    private void printInvoice(Invoice invoice) { /* ... */ }
}
