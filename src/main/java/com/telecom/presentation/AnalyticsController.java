package com.telecom.presentation;

import com.telecom.models.Customer;
import com.telecom.models.Invoice;
import com.telecom.models.Plan;
import com.telecom.models.UsageRecord;
import com.telecom.repository.interfaces.InvoiceRepo;
import com.telecom.repository.interfaces.PlanRepo;
import com.telecom.repository.interfaces.UsageRecordRepo;
import com.telecom.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console viewer for all analytics and reporting operations.
 */
@RequiredArgsConstructor
public class AnalyticsController {
    private final Scanner sc;
    private final AnalyticsService analyticsService;
    private final UsageRecordRepo usageRecordRepo;
    private final InvoiceRepo invoiceRepo;
    private final PlanRepo planRepo;

    /**
     * Shows the main menu for analytics reports.
     */
    public void showAnalyticsMenu() {
        while (true) {
            System.out.println("\n--- Analytics & Reports ---");
            System.out.println("1. Top N Data Users");
            System.out.println("2. Average Revenue Per User (ARPU) by Plan");
            System.out.println("3. Overage Charge Distribution");
            System.out.println("4. Credit Risk Detection");
            System.out.println("5. Plan Recommendation");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: displayTopNDataUsers(); break;
                case 2: displayArpuByPlan(); break;
                case 3: displayOverageDistribution(); break;
                case 4: displayCreditRisk(); break;
                case 5: displayPlanRecommendation(); break;
                case 0: return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayTopNDataUsers() {
        System.out.print("Enter N: ");
        int n = Integer.parseInt(sc.nextLine());
        List<UsageRecord> usage = usageRecordRepo.findAll();
        List<Customer> topUsers = analyticsService.topDataUsers(usage, n);
        System.out.println("\n--- Top " + n + " Data Users ---");
        topUsers.forEach(c -> System.out.printf("Customer ID: %s%n", c.getId()));
    }

    private void displayArpuByPlan() {
        List<Invoice> invoices = invoiceRepo.findAll();
        Map<String, Double> arpu = analyticsService.arpuByPlan(invoices);
        if (arpu.isEmpty()){
            System.out.println("No Data found.");
        }
        System.out.println("\n--- Average Revenue Per User (ARPU) by Plan ---");
        arpu.forEach((planId, avg) -> System.out.printf("Plan ID %s: $%.2f%n", planId, avg));
    }

    private void displayOverageDistribution() {
        List<Invoice> invoices = invoiceRepo.findAll();
        Map<String, Object> dist = analyticsService.overageDistribution(invoices);
        System.out.println("\n--- Overage Charge Distribution ---");
        System.out.printf("Total Invoices with Overage: %d%n", dist.get("count"));
        System.out.printf("Total Overage Amount: $%.2f%n", dist.get("total"));
        System.out.printf("Average Overage Amount: $%.2f%n", dist.get("average"));
    }

    private void displayCreditRisk() {
        // This is a simplified example. A real implementation would need a Customer repository.
        System.out.println("\n--- Credit Risk Detection ---");
        System.out.println("NOTE: This is a simplified report.");
        List<Invoice> invoices = invoiceRepo.findAll();
        // The service method expects a list of customers, which we don't have a repo for yet.
        // We will pass an empty list for now.
        List<Customer> riskyCustomers = analyticsService.detectCreditRisk(invoices, List.of());
        if (riskyCustomers.isEmpty()) {
            System.out.println("No customers with unpaid invoices found.");
        } else {
            riskyCustomers.forEach(c -> System.out.printf("Customer ID %s is a potential credit risk.%n", c.getId()));
        }
    }

    private void displayPlanRecommendation() {
        List<Invoice> invoices = invoiceRepo.findAll();
        List<Plan> plans = planRepo.findAll();
        Map<String, String> recommendations = analyticsService.recommendPlans(invoices, plans);
        System.out.println("\n--- Plan Upgrade Recommendations ---");
        if (recommendations.isEmpty()) {
            System.out.println("No plan recommendations at this time.");
        } else {
            recommendations.forEach((subId, planName) -> 
                System.out.printf("Subscription %s should consider upgrading to '%s' due to frequent overages.%n", subId, planName));
        }
    }
}
