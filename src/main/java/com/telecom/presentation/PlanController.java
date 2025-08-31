package com.telecom.presentation;

import com.telecom.models.Plan;
import com.telecom.service.interfaces.PlanService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;

/**
 * Console viewer for all plan-based operations
 */
@RequiredArgsConstructor
public class PlanController {
    private final Scanner sc;
    private final PlanService planService;

    /**
     * CLI to display all available plans.
     */
    public void showAllPlans() {
        List<Plan> plans = planService.getAllPlans();
        System.out.println("\n--- Available Telecom Plans ---");
        plans.forEach(plan -> {
            System.out.printf("ID: %s, Name: %s, Rental: $%.2f/month%n", plan.getId(), plan.getName(), plan.getMonthlyRental());
            System.out.printf("  Data: %.2f MB, Voice: %d mins, SMS: %d%n", plan.getDataAllowanceMb(), plan.getVoiceAllowanceMin(), plan.getSmsAllowance());
            if (plan.isFamilyPlan()) {
                System.out.println("  Type: Family Plan");
            }
            System.out.println("-----------------------------------");
        });
    }

    /**
     * CLI to add a new plan.
     */
    public void addNewPlan() {
        try {
            System.out.println("\n--- Add a New Plan ---");
            System.out.print("Enter Plan ID: ");
            String id = sc.nextLine();
            System.out.print("Enter Plan Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Monthly Rental: ");
            double rental = Double.parseDouble(sc.nextLine());
            System.out.print("Enter Data Allowance (MB): ");
            double data = Double.parseDouble(sc.nextLine());
            System.out.print("Enter Voice Allowance (Minutes): ");
            int voice = Integer.parseInt(sc.nextLine());
            System.out.print("Enter SMS Allowance: ");
            int sms = Integer.parseInt(sc.nextLine());
            System.out.print("Is this a family plan? (true/false): ");
            boolean isFamily = Boolean.parseBoolean(sc.nextLine());

            Plan newPlan = Plan.builder()
                    .id(id)
                    .name(name)
                    .monthlyRental(rental)
                    .dataAllowanceMb(data)
                    .voiceAllowanceMin(voice)
                    .smsAllowance(sms)
                    .isFamilyPlan(isFamily)
                    // Setting default overage rates and other properties for simplicity
                    .dataOverageRate(0.15)
                    .voiceOverageRate(0.1)
                    .smsOverageRate(0.05)
                    .build();

            planService.addPlan(newPlan);
            System.out.println("Plan added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void updateExistingPlan() {
        try {
            System.out.println("\n--- Update a Plan ---");
            showAllPlans();
            System.out.print("Enter the ID of the plan to update: ");
            String id = sc.nextLine();
            Plan plan = planService.getPlan(id);

            System.out.printf("Enter new name (current: %s): ", plan.getName());
            String newName = sc.nextLine();
            if (!newName.isEmpty()) {
                plan.setName(newName);
            }

            System.out.printf("Enter new monthly rental (current: %.2f): ", plan.getMonthlyRental());
            String newRentalStr = sc.nextLine();
            if (!newRentalStr.isEmpty()) {
                plan.setMonthlyRental(Double.parseDouble(newRentalStr));
            }

            System.out.printf("Is this a family plan? (current: %s): ", plan.isFamilyPlan());
            String isFamilyStr = sc.nextLine();
            if (!isFamilyStr.isEmpty()) {
                plan.setFamilyPlan(Boolean.parseBoolean(isFamilyStr));
            }

            planService.updatePlan(plan);
            System.out.println("Plan updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void deleteExistingPlan() {
        try {
            System.out.println("\n--- Delete a Plan ---");
            showAllPlans();
            System.out.print("Enter the ID of the plan to delete: ");
            String id = sc.nextLine();

            planService.deletePlan(id);
            System.out.println("Plan deleted successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
