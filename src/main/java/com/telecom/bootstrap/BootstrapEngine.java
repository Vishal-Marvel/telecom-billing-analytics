package com.telecom.bootstrap;

import com.telecom.models.*;
import com.telecom.models.enums.Role;
import com.telecom.repository.interfaces.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Initializes the in-memory database with sample data for demonstration.
 */
@RequiredArgsConstructor
public class BootstrapEngine {

    private final UserRepo userRepo;
    private final CustomerRepo customerRepo;
    private final PlanRepo planRepo;
    private final SubscriptionRepo subscriptionRepo;
    private final UsageRecordRepo usageRecordRepo;

    public void run() {
        System.out.println("Bootstrapping data...");

        Supplier<String> getUUID = () -> UUID.randomUUID().toString().substring(0, 3);

        // Create Plans
        Plan individualPlan = Plan.builder().id("P" + getUUID.get()).name("Solo Basic").monthlyRental(30).dataAllowanceMb(5000)
                .voiceAllowanceMin(200).smsAllowance(100).isFamilyPlan(false).dataOverageRate(0.2).voiceOverageRate(0.1)
                .smsOverageRate(0.05).build();
        Plan familyPlan = Plan.builder().id("P" + getUUID.get()).name("Family Plus").monthlyRental(80).dataAllowanceMb(25000)
                .voiceAllowanceMin(500).smsAllowance(500).isFamilyPlan(true).familyShareCap(0.6).dataOverageRate(0.15)
                .voiceOverageRate(0.1).smsOverageRate(0.05).build();
        planRepo.save(individualPlan);
        planRepo.save(familyPlan);

        // Create Customers and Users
        Customer cust1 = new Customer("C" + getUUID.get(), "Alice", "alice@email.com", null, false);
        Customer cust2 = new Customer("C" + getUUID.get(), "Bob", "bob@email.com", "C1", false);
        customerRepo.save(cust1);
        customerRepo.save(cust2);

        User adminUser = User.builder().username("admin").password("admin").role(Role.ADMIN).build();
        User user1 = User.builder().username("alice").password("pass").role(Role.CUSTOMER).customerId(cust1.getId()).build();
        User user2 = User.builder().username("bob").password("pass").role(Role.CUSTOMER).customerId(cust2.getId()).build();
        userRepo.createUser(adminUser);
        userRepo.createUser(user1);
        userRepo.createUser(user2);

        // Create Subscriptions
        String familyId = "F" + getUUID.get();
        Subscription sub1 = Subscription.builder().id("S" + getUUID.get()).customerId(cust1.getId()).planId(individualPlan.getId()).phoneNumber("9876543210")
                .startDate(LocalDate.now().minusMonths(2)).build();
        Subscription sub2 = Subscription.builder().id("S" + getUUID.get()).customerId(cust2.getId()).planId(familyPlan.getId()).phoneNumber("9876543211")
                .familyId(familyId).startDate(LocalDate.now().minusMonths(1)).build();
        Subscription sub3 = Subscription.builder().id("S" + getUUID.get()).customerId(cust2.getId()).planId(familyPlan.getId()).phoneNumber("9876543212")
                .familyId(familyId).startDate(LocalDate.now().minusMonths(1)).build();
        subscriptionRepo.save(sub1);
        subscriptionRepo.save(sub2);
        subscriptionRepo.save(sub3);

        // Create Usage Records
        // Usage for individual plan (S1) - with overage
        usageRecordRepo.save(
                new UsageRecord(sub1.getId(), null, LocalDateTime.now().minusDays(10), 6000, 150, 50, false, false, false));
        // Usage for family plan (S2, S3)
        // S2 uses a lot of data (triggers fairness surcharge), S3 uses less
        usageRecordRepo.save(new UsageRecord(sub2.getId(), familyId, LocalDateTime.now().minusDays(5), 20000, 100, 100, false,
                false, false)); // > 60% of 25000
        usageRecordRepo.save(
                new UsageRecord(sub3.getId(), familyId, LocalDateTime.now().minusDays(3), 6000, 50, 50, true, false, true)); // Roaming

        System.out.println("Bootstrap completed.");
    }
}