package com.telecom.bootstrap;

import com.telecom.models.*;
import com.telecom.models.enums.Role;
import com.telecom.repository.interfaces.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

        // Create Plans
        Plan individualPlan = Plan.builder().id("P1").name("Solo Basic").monthlyRental(30).dataAllowanceMb(5000)
                .voiceAllowanceMin(200).smsAllowance(100).isFamilyPlan(false).dataOverageRate(0.2).voiceOverageRate(0.1)
                .smsOverageRate(0.05).build();
        Plan familyPlan = Plan.builder().id("P2").name("Family Plus").monthlyRental(80).dataAllowanceMb(25000)
                .voiceAllowanceMin(500).smsAllowance(500).isFamilyPlan(true).familyShareCap(0.6).dataOverageRate(0.15)
                .voiceOverageRate(0.1).smsOverageRate(0.05).build();
        planRepo.save(individualPlan);
        planRepo.save(familyPlan);

        // Create Customers and Users
        Customer cust1 = new Customer("C1", "Alice", "alice@email.com", null, false);
        Customer cust2 = new Customer("C2", "Bob", "bob@email.com", "C1", false);
        customerRepo.save(cust1);
        customerRepo.save(cust2);

        User adminUser = User.builder().username("admin").password("admin").role(Role.ADMIN).build();
        User user1 = User.builder().username("alice").password("pass").role(Role.CUSTOMER).customerId("C1").build();
        User user2 = User.builder().username("bob").password("pass").role(Role.CUSTOMER).customerId("C2").build();
        userRepo.createUser(adminUser);
        userRepo.createUser(user1);
        userRepo.createUser(user2);

        // Create Subscriptions
        String familyId = "F1";
        Subscription sub1 = Subscription.builder().id("S1").customerId("C1").planId("P1").phoneNumber("111-1111")
                .startDate(LocalDate.now().minusMonths(2)).build();
        Subscription sub2 = Subscription.builder().id("S2").customerId("C2").planId("P2").phoneNumber("222-2222")
                .familyId(familyId).startDate(LocalDate.now().minusMonths(1)).build();
        Subscription sub3 = Subscription.builder().id("S3").customerId("C2").planId("P2").phoneNumber("333-3333")
                .familyId(familyId).startDate(LocalDate.now().minusMonths(1)).build();
        subscriptionRepo.save(sub1);
        subscriptionRepo.save(sub2);
        subscriptionRepo.save(sub3);

        // Create Usage Records
        // Usage for individual plan (S1) - with overage
        usageRecordRepo.save(
                new UsageRecord("S1", null, LocalDateTime.now().minusDays(10), 6000, 150, 50, false, false, false));
        // Usage for family plan (S2, S3)
        // S2 uses a lot of data (triggers fairness surcharge), S3 uses less
        usageRecordRepo.save(new UsageRecord("S2", familyId, LocalDateTime.now().minusDays(5), 20000, 100, 100, false,
                false, false)); // > 60% of 25000
        usageRecordRepo.save(
                new UsageRecord("S3", familyId, LocalDateTime.now().minusDays(3), 6000, 50, 50, true, false, true)); // Roaming
                                                                                                                     // and
                                                                                                                     // night
                                                                                                                     // usage

        System.out.println("Bootstrap completed.");
    }
}