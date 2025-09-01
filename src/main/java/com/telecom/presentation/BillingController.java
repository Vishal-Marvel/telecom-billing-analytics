package com.telecom.presentation;

import com.telecom.models.*;
import com.telecom.repository.interfaces.UsageRecordRepo;
import com.telecom.service.interfaces.BillingService;
import com.telecom.service.interfaces.PlanService;
import com.telecom.service.interfaces.SubscriptionService;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Console viewer for all billing and invoice-related operations.
 */
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;
    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final UsageRecordRepo usageRecordRepo;

    /**
     * Main CLI menu for the billing cycle.
     *
     * @param user The currently authenticated user.
     */
    public void runBillingCycle(User user) {
        System.out.println("\n--- Run Monthly Billing Cycle ---");


        List<Subscription> subscriptions = subscriptionService.getCustomerSubscriptions(user.getCustomerId());
        List<UsageRecord> allUsage = usageRecordRepo.findAll(); // Get all usage for the period

        System.out.println("\n--- Generated Invoices ---");
        for (Subscription sub : subscriptions) {
            Plan plan = planService.getPlan(sub.getPlanId());
            // Filter usage for this specific member
            List<UsageRecord> memberUsage = allUsage.stream()
                    .filter(u -> u.getSubscriptionId().equals(sub.getId()))
                    .toList();

            try {
                Invoice invoice = billingService.generateInvoice(sub, plan, memberUsage, allUsage);
                printInvoice(invoice);
            } catch (Exception e) {
                System.err.printf("Could not generate invoice for subscription %s: %s%n", sub.getId(), e.getMessage());
            }
        }

    }

    /**
     * Prints a formatted invoice to the console.
     *
     * @param invoice The invoice to print.
     */
    public void printInvoice(Invoice invoice) {
        System.out.println("===================================");
        System.out.println("            CUSTOMER INVOICE       ");
        System.out.println("===================================");

        // Header
        System.out.printf("Invoice ID       : %s%n", invoice.getId());
        System.out.printf("Subscription ID  : %s%n", invoice.getSubscriptionId());
        System.out.printf("Billing Cycle    : %s%n",
                invoice.getBillingCycle().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        System.out.println("-----------------------------------");

        // Charges Breakdown
        System.out.printf("Base Rental      : $%.2f%n", invoice.getBaseRental());
        System.out.printf("Overage Charges  : $%.2f%n", invoice.getOverageCharges());
        System.out.printf("Roaming Charges  : $%.2f%n", invoice.getRoamingCharges());

        if (invoice.getDiscounts() < 0) {
            System.out.printf("Surcharge        : $%.2f%n", -invoice.getDiscounts());
        } else if (invoice.getDiscounts() > 0) {
            System.out.printf("Discounts        : -$%.2f%n", invoice.getDiscounts());
        }

        System.out.printf("Taxes (GST)      : $%.2f%n", invoice.getTaxes());

        System.out.println("-----------------------------------");

        // Total
        System.out.printf("TOTAL AMOUNT     : $%.2f%n", invoice.getTotalAmount());
        System.out.printf("Status           : %s%n", invoice.isPaid() ? "Paid ✅" : "Unpaid ❌");

        System.out.println("===================================");
        System.out.println();
    }
}
