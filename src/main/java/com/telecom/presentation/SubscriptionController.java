package com.telecom.presentation;

import com.telecom.exceptions.InvalidPhoneNumberFormatException;
import com.telecom.models.Family;
import com.telecom.models.Plan;
import com.telecom.models.Subscription;
import com.telecom.models.User;
import com.telecom.service.interfaces.FamilyService;
import com.telecom.service.interfaces.PlanService;
import com.telecom.service.interfaces.SubscriptionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Console viewer for all subscription-based operations.
 */
@RequiredArgsConstructor
public class SubscriptionController {
    private final Scanner sc;
    private final SubscriptionService subscriptionService;
    private final PlanService planService; // Needed to show plan details
    private final FamilyService familyService;

    /**
     * Displays all subscriptions for the logged-in user.
     *
     * @param user The currently authenticated user.
     */
    public void showCustomerSubscriptions(User user) {
        List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());
        System.out.println("\n--- Your Subscriptions ---");
        if (subscriptions.isEmpty()) {
            System.out.println("You have no active subscriptions.");
            return;
        }
        subscriptions.forEach(sub -> {
            Plan plan = planService.getPlan(sub.getPlanId());
            System.out.printf("ID: %s, Phone: %s%n", sub.getId(), sub.getPhoneNumber());
            System.out.printf("  Plan: %s ($%.2f/month)%n", plan.getName(), plan.getMonthlyRental());
            System.out.printf("  Status: Active since %s%n", sub.getStartDate());
            if (sub.isMnpPending()) {
                System.out.printf("  MNP Status: Port-out pending to %s since %s%n", sub.getTargetOperator(), sub.getMnpRequestDate());
            }
            System.out.println("-----------------------------------");
        });
    }

    /**
     * CLI to add a new subscription for the logged-in user.
     *
     * @param user The currently authenticated user.
     */
    public void addNewSubscription(User user) {
        System.out.println("\n--- Add a New Subscription ---");
        new PlanController(sc, planService).showAllPlans(); // Show available plans
        System.out.print("Enter Plan ID to subscribe to: ");
        String planId = sc.nextLine();
        Plan plan = planService.getPlan(planId);
        System.out.print("Enter new Phone Number: ");
        String phone = sc.nextLine();

        if (!phone.matches("^[6789]\\d{9}$")) {
            throw new InvalidPhoneNumberFormatException("Valid phone number: " + phone);
        }

        String subId = "S" + UUID.randomUUID().toString().substring(0, 3);
        String familyId = null;
        if (plan.isFamilyPlan()) {
            familyId = "F" + UUID.randomUUID().toString().substring(0, 3);
            familyService.createFamily(Family.builder()
                    .customerIds(new ArrayList<>())
                    .familyId(familyId)
                    .build());
            familyService.addFamilyMember(familyId, user.getCustomerId());
        }


        Subscription newSubscription = Subscription.builder()
                .id(subId)
                .customerId(user.getCustomerId())
                .phoneNumber(phone)
                .planId(planId)
                .startDate(LocalDate.now())
                .familyId(familyId)
                .build();

        subscriptionService.addSubscription(newSubscription);
        System.out.println("Subscription added successfully!");
    }

    /**
     * CLI to initiate an MNP port-out request.
     *
     * @param user The currently authenticated user.
     */
    public void initiateMnp(User user) {
        System.out.println("\n--- Mobile Number Portability (MNP) Port-Out ---");
        showCustomerSubscriptions(user);
        System.out.print("Enter Subscription ID to port out: ");
        String subId = sc.nextLine();
        System.out.print("Enter Target Operator Name: ");
        String operator = sc.nextLine();

        try {
            subscriptionService.requestMNP(subId, operator);
            System.out.println("MNP request submitted successfully. Your plan cannot be changed until this is resolved.");
        } catch (Exception e) {
            System.err.println("Error submitting MNP request: " + e.getMessage());
        }
    }
}
