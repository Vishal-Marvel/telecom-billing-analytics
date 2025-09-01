package com.telecom.presentation;

import com.telecom.exceptions.InvalidChoiceException;
import com.telecom.exceptions.PlanNotFoundException;
import com.telecom.models.*;
import com.telecom.repository.interfaces.InvoiceRepo;
import com.telecom.repository.interfaces.UsageRecordRepo;
import com.telecom.service.interfaces.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.naming.NameNotFoundException;

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
    private final FamilyService familyService;
    private final CustomerService customerService;
    private final BillingController billingController;
    private final SubscriptionController subscriptionController;
    private final BillingService billingService;

    /**
     * Main menu for Customer users.
     *
     * @param user The currently authenticated user.
     */
    public void showCustomerMenu(User user) {
        while (true) {
            try {
                System.out.println("\n--- Customer Menu ---");
                System.out.println("1. View My Subscriptions");
                System.out.println("2. Add Usage (Simulate)");
                System.out.println("3. Change Plan");
                System.out.println("4. View Usage History");
                System.out.println("5. View My Bills");
                System.out.println("6. Pay a Bill");
                System.out.println("7. Add Family Member");
                System.out.println("8. View My Family Members");
                System.out.println("9. Initiate a MNP");
                System.out.println("10. Add a new Subscription");
                System.out.println("0. Logout");
                System.out.print("Select an option: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        subscriptionController.showCustomerSubscriptions(user);
                        break;
                    case 2:
                        addUsage(user);
                        break;
                    case 3:
                        changePlan(user);
                        break;
                    case 4:
                        viewUsageHistory(user);
                        break;
                    case 5:
                        viewMyBills(user);
                        break;
                    case 6:
                        payBill(user);
                        break;
                    case 7:
                        addFamilyMember(user);
                        break;
                    case 8:
                        viewMyFamilyMembers(user);
                        break;
                    case 9:
                        subscriptionController.initiateMnp(user);
                        break;
                    case 10:
                        subscriptionController.addNewSubscription(user);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void addFamilyMember(User currentUser) {
        try {
            System.out.println("\n--- Add a Family Member ---");

            // Find the current user's family subscription
            Subscription primarySub = subscriptionService.getCustomerSubscriptions(currentUser.getCustomerId())
                    .stream().filter(s -> s.getFamilyId() != null).findFirst()
                    .orElse(null);

            if (primarySub == null) {
                System.out.println("You do not have a family plan subscription.");
                return;
            }

            System.out.print("Enter the Customer ID of the existing customer to add to your family plan: ");
            String customerId = sc.nextLine();

            // Check if the customer exists
            Customer existingCustomer = customerService.getCustomer(customerId);
            if (existingCustomer == null) {
                System.out.println("Customer with ID " + customerId + " not found.");
                return;
            }

            // Add to family
            familyService.addFamilyMember(primarySub.getFamilyId(), customerId);

            System.out.print("Enter phone number for the new family member's subscription: ");
            String phone = sc.nextLine();

            // Create subscription for the new member
            Subscription newSubscription = Subscription.builder()
                    .id("S" + (int) (Math.random() * 1000))
                    .customerId(customerId)
                    .phoneNumber(phone)
                    .planId(primarySub.getPlanId()) // Same plan as primary
                    .familyId(primarySub.getFamilyId())
                    .startDate(LocalDate.now())
                    .build();
            subscriptionService.addSubscription(newSubscription);

            System.out.println("Family member added successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void viewMyFamilyMembers(User currentUser) {
        try {
            System.out.println("\n--- My Family Members ---");

            // Find the current user's family subscription
            Subscription primarySub = subscriptionService.getCustomerSubscriptions(currentUser.getCustomerId())
                    .stream().filter(s -> s.getFamilyId() != null).findFirst()
                    .orElse(null);

            if (primarySub == null) {
                System.out.println("You do not have a family plan subscription.");
                return;
            }

            Family family = familyService.getFamilyById(primarySub.getFamilyId());
            if (family == null || family.getCustomerIds().isEmpty()) {
                System.out.println("No family members found.");
                return;
            }

            System.out.println("Your Family Members:");
            for (String memberId : family.getCustomerIds()) {
                Customer member = customerService.getCustomer(memberId);
                if (member != null) {
                    System.out.printf("- Customer ID: %s, Name: %s, Email: %s%n",
                            member.getId(), member.getName(), member.getEmail());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // other methods...
    private void addUsage(User user) {
        try {
            System.out.println("\n--- Add Usage (Simulate) ---");

            List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());
            if (subscriptions.isEmpty()) {
                System.out.println("You have no subscriptions.");
                return;
            }

            System.out.println("Your Subscriptions:");
            for (int i = 0; i < subscriptions.size(); i++) {
                System.out.printf("%d. %s (%s)%n", i + 1, subscriptions.get(i).getId(),
                        subscriptions.get(i).getPhoneNumber());
            }

            System.out.print("Select a subscription to add usage to: ");
            int subChoice = Integer.parseInt(sc.nextLine());
            if (subChoice < 1 || subChoice > subscriptions.size()) {
                System.out.println("Invalid choice.");
                return;
            }
            Subscription selectedSub = subscriptions.get(subChoice - 1);

            // Pick random usage type (1=Data, 2=Voice, 3=SMS)
            int usageType = ThreadLocalRandom.current().nextInt(1, 4);

            // Generate random amount based on type
            double amount = switch (usageType) {
                case 1 -> // Data in MB
                        ThreadLocalRandom.current().nextDouble(50, 2048); // between 50MB and 2GB
                case 2 -> // Voice minutes
                        ThreadLocalRandom.current().nextInt(1, 300); // between 1 and 300 minutes
                case 3 -> // SMS count
                        ThreadLocalRandom.current().nextInt(1, 100); // between 1 and 100 SMS
                default -> 0;
            };

            // Build usage record
            UsageRecord.UsageRecordBuilder usageBuilder = UsageRecord.builder()
                    .subscriptionId(selectedSub.getId())
                    .familyId(selectedSub.getFamilyId())
                    .timestamp(LocalDateTime.now());

            switch (usageType) {
                case 1:
                    usageBuilder.data(amount);
                    System.out.println("Generated Data Usage: " + amount + " MB");
                    break;
                case 2:
                    usageBuilder.voiceMinutes((int) amount);
                    System.out.println("Generated Voice Usage: " + (int) amount + " minutes");
                    break;
                case 3:
                    usageBuilder.smsCount((int) amount);
                    System.out.println("Generated SMS Usage: " + (int) amount + " messages");
                    break;
            }

            usageRecordRepo.save(usageBuilder.build());
            System.out.println("Usage added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void changePlan(User user) {
        try {
            System.out.println("\n--- Change Plan ---");

            List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());
            if (subscriptions.isEmpty()) {
                System.out.println("You have no subscriptions.");
                return;
            }

            System.out.println("Your Subscriptions:");
            for (int i = 0; i < subscriptions.size(); i++) {
                System.out.printf("%d. %s (%s)%n", i + 1, subscriptions.get(i).getId(),
                        subscriptions.get(i).getPhoneNumber());
            }

            System.out.print("Select a subscription to change the plan for: ");
            int subChoice = Integer.parseInt(sc.nextLine());
            if (subChoice < 1 || subChoice > subscriptions.size()) {
                throw new InvalidChoiceException("Invalid choice.");
            }
            Subscription selectedSub = subscriptions.get(subChoice - 1);

            new PlanController(sc, planService).showAllPlans();

            System.out.print("Enter the ID of the new plan: ");
            String newPlanId = sc.nextLine();

            Plan newPlan = planService.getPlan(newPlanId);
            if (newPlan == null) {
                throw new PlanNotFoundException("Plan not found.");
            }

            subscriptionService.changePlan(selectedSub.getId(), newPlan.getId());

            System.out.println("Plan changed successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void viewUsageHistory(User user) {
        try {
            System.out.println("\n--- View Usage History ---");

            List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());
            if (subscriptions.isEmpty()) {
                System.out.println("You have no subscriptions.");
                return;
            }

            System.out.println("Your Subscriptions:");
            for (int i = 0; i < subscriptions.size(); i++) {
                System.out.printf("%d. %s (%s)%n", i + 1, subscriptions.get(i).getId(),
                        subscriptions.get(i).getPhoneNumber());
            }

            System.out.print("Select a subscription to view usage history for: ");
            int subChoice = Integer.parseInt(sc.nextLine());
            if (subChoice < 1 || subChoice > subscriptions.size()) {
                System.out.println("Invalid choice.");
                return;
            }
            Subscription selectedSub = subscriptions.get(subChoice - 1);

            List<UsageRecord> usageRecords = usageRecordRepo.findBySubscriptionId(selectedSub.getId());

            if (usageRecords.isEmpty()) {
                System.out.println("No usage history found for this subscription.");
                return;
            }

            System.out.println("\n--- Usage History for " + selectedSub.getId() + " ---");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            for (UsageRecord record : usageRecords) {
                String formattedTime = record.getTimestamp().format(formatter);

                System.out.printf("Timestamp: %s, Data: %.2f MB, Voice: %d mins, SMS: %d%n",
                        formattedTime, record.getData(), record.getVoiceMinutes(), record.getSmsCount());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void viewMyBills(User user) {
        try {
            System.out.println("\n--- View My Bills ---");

            List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());
            if (subscriptions.isEmpty()) {
                System.out.println("You have no subscriptions.");
                return;
            }
            List<Invoice> invoices = new ArrayList<>();
            for (Subscription sub : subscriptions) {
                invoices.addAll(invoiceRepo.findBySubscriptionId(sub.getId()));
            }

            if (invoices.isEmpty()) {
                System.out.println("No bills found.");
                return;
            }

            for (Invoice invoice : invoices) {
                System.out.printf("Invoice ID: %s, Due Date: %s, Amount: %.2f, Paid: %s%n",
                        invoice.getId(), invoice.getBillingCycle().atEndOfMonth().plusDays(15),
                        invoice.getTotalAmount(), invoice.isPaid() ? "Yes" : "No");
            }

            System.out.print("\nDo you want to see the details of a specific invoice? (yes/no): ");
            String choice = sc.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("Enter the Invoice ID: ");
                String invoiceId = sc.nextLine();
                Invoice selectedInvoice = invoiceRepo.findById(invoiceId)
                        .orElseThrow(() -> new NameNotFoundException("Invoice Not Found"));
                if (selectedInvoice != null && subscriptions.stream()
                        .anyMatch(sub -> sub.getId().equals(selectedInvoice.getSubscriptionId()))) {
                    billingController
                            .printInvoice(selectedInvoice);
                } else {
                    System.out.println("Invoice not found or you don't have permission to view it.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void payBill(User user) {
        try {
            System.out.println("\n--- Pay a Bill ---");
            billingController
                    .runBillingCycle(user);
            List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());

            List<Invoice> unpaidInvoices = new ArrayList<>();
            for (Subscription sub : subscriptions) {
                unpaidInvoices.addAll(invoiceRepo.findBySubscriptionId(sub.getId())
                        .stream().filter(inv -> !inv.isPaid()).toList());
            }

            if (unpaidInvoices.isEmpty()) {
                System.out.println("You have no unpaid bills.");
                return;
            }

            System.out.println("Your Unpaid Bills:");
            for (Invoice invoice : unpaidInvoices) {
                System.out.printf("Invoice ID: %s, Due Date: %s, Amount: %.2f%n",
                        invoice.getId(), invoice.getBillingCycle().atEndOfMonth().plusDays(15),
                        invoice.getTotalAmount());
            }

            System.out.print("Enter the Invoice ID to pay: ");
            String invoiceId = sc.nextLine();

            Invoice selectedInvoice = unpaidInvoices.stream()
                    .filter(inv -> inv.getId().equals(invoiceId))
                    .findFirst()
                    .orElse(null);

            if (selectedInvoice == null) {
                throw new InvalidChoiceException("Invoice not found or it is already paid.");
            }

            billingService.payInvoice(selectedInvoice.getId());

            System.out.println("Bill paid successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

}
