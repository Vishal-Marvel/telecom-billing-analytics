package com.telecom.bootstrap;

import com.telecom.exceptions.PlanNotFoundException;
import com.telecom.models.*;
import com.telecom.models.enums.Role;
import com.telecom.presentation.BillingController;
import com.telecom.repository.impl.*;
import com.telecom.repository.interfaces.*;
import com.telecom.service.impl.BillingServiceImpl;
import com.telecom.service.interfaces.BillingService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Initializes the in-memory database with sample data for demonstration.
 */
@RequiredArgsConstructor
public class BootstrapEngine {

    private final UserRepo userRepo;
    private final PlanRepo planRepo;
    private final CustomerRepo customerRepo;
    private final SubscriptionRepo subscriptionRepo;
    private final UsageRecordRepo usageRecordRepo;
    private final BillingService billingService;

    public void run() {
        System.out.println("Bootstrapping data...");

        Supplier<String> getUUID = () -> UUID.randomUUID().toString().substring(0, 3);

        // --- Create Plans ---
        Plan individualPlan = Plan.builder().id("P" + getUUID.get()).name("Solo Basic").monthlyRental(30).dataAllowanceMb(5000)
                .voiceAllowanceMin(200).smsAllowance(100).isFamilyPlan(false).dataOverageRate(0.2).voiceOverageRate(0.1)
                .smsOverageRate(0.05).build();

        Plan familyPlan = Plan.builder().id("P" + getUUID.get()).name("Family Plus").monthlyRental(80).dataAllowanceMb(25000)
                .voiceAllowanceMin(500).smsAllowance(500).isFamilyPlan(true).familyShareCap(0.6).dataOverageRate(0.15)
                .voiceOverageRate(0.1).smsOverageRate(0.05).build();

        Plan premiumPlan = Plan.builder().id("P" + getUUID.get()).name("Premium Unlimited").monthlyRental(120).dataAllowanceMb(100000)
                .voiceAllowanceMin(2000).smsAllowance(2000).isFamilyPlan(false).dataOverageRate(0.05).voiceOverageRate(0.05)
                .smsOverageRate(0.02).build();

        Plan studentPlan = Plan.builder().id("P" + getUUID.get()).name("Student Saver").monthlyRental(20).dataAllowanceMb(3000)
                .voiceAllowanceMin(100).smsAllowance(50).isFamilyPlan(false).dataOverageRate(0.25).voiceOverageRate(0.15)
                .smsOverageRate(0.10).build();

        planRepo.save(individualPlan);
        planRepo.save(familyPlan);
        planRepo.save(premiumPlan);
        planRepo.save(studentPlan);

        // --- Create Customers ---
        Customer cust1 = new Customer("C" + getUUID.get(), "Alice", "alice@email.com", null, false);
        Customer cust2 = new Customer("C" + getUUID.get(), "Bob", "bob@email.com", cust1.getId(), false);
        Customer cust3 = new Customer("C" + getUUID.get(), "Charlie", "charlie@email.com", null, false);
        Customer cust4 = new Customer("C" + getUUID.get(), "Diana", "diana@email.com", null, false);
        Customer cust5 = new Customer("C" + getUUID.get(), "Ethan", "ethan@email.com", null, false);

        customerRepo.save(cust1);
        customerRepo.save(cust2);
        customerRepo.save(cust3);
        customerRepo.save(cust4);
        customerRepo.save(cust5);

        // --- Users ---

        userRepo.createUser(User.builder().username("admin").password("admin").role(Role.ADMIN).build());
        userRepo.createUser(User.builder().username("alice").password("pass").role(Role.CUSTOMER).customerId(cust1.getId()).build());
        userRepo.createUser(User.builder().username("bob").password("pass").role(Role.CUSTOMER).customerId(cust2.getId()).build());
        userRepo.createUser(User.builder().username("charlie").password("pass").role(Role.CUSTOMER).customerId(cust3.getId()).build());
        userRepo.createUser(User.builder().username("diana").password("pass").role(Role.CUSTOMER).customerId(cust4.getId()).build());
        userRepo.createUser(User.builder().username("ethan").password("pass").role(Role.CUSTOMER).customerId(cust5.getId()).build());

        // --- Subscriptions ---
        String familyId1 = "F" + getUUID.get();
        String familyId2 = "F" + getUUID.get();

        Subscription sub1 = Subscription.builder().id("S" + getUUID.get()).customerId(cust1.getId()).planId(individualPlan.getId()).phoneNumber("9876543210")
                .startDate(LocalDate.now().minusMonths(2)).build();

        Subscription sub2 = Subscription.builder().id("S" + getUUID.get()).customerId(cust2.getId()).planId(familyPlan.getId()).phoneNumber("9876543211")
                .familyId(familyId1).startDate(LocalDate.now().minusMonths(1)).build();

        Subscription sub3 = Subscription.builder().id("S" + getUUID.get()).customerId(cust2.getId()).planId(familyPlan.getId()).phoneNumber("9876543212")
                .familyId(familyId1).startDate(LocalDate.now().minusMonths(1)).build();

        Subscription sub4 = Subscription.builder().id("S" + getUUID.get()).customerId(cust3.getId()).planId(premiumPlan.getId()).phoneNumber("9876543213")
                .startDate(LocalDate.now().minusMonths(3)).build();

        Subscription sub5 = Subscription.builder().id("S" + getUUID.get()).customerId(cust4.getId()).planId(studentPlan.getId()).phoneNumber("9876543214")
                .startDate(LocalDate.now().minusWeeks(2)).build();

        Subscription sub6 = Subscription.builder().id("S" + getUUID.get()).customerId(cust5.getId()).planId(familyPlan.getId()).phoneNumber("9876543215")
                .familyId(familyId2).startDate(LocalDate.now().minusWeeks(5)).build();

        Subscription sub7 = Subscription.builder().id("S" + getUUID.get()).customerId(cust5.getId()).planId(familyPlan.getId()).phoneNumber("9876543216")
                .familyId(familyId2).startDate(LocalDate.now().minusWeeks(5)).build();

        subscriptionRepo.save(sub1);
        subscriptionRepo.save(sub2);
        subscriptionRepo.save(sub3);
        subscriptionRepo.save(sub4);
        subscriptionRepo.save(sub5);
        subscriptionRepo.save(sub6);
        subscriptionRepo.save(sub7);

        // --- Usage Records ---
        // Individual (over data limit)
        usageRecordRepo.save(new UsageRecord(sub1.getId(), null, LocalDateTime.now().minusDays(10), 6000, 150, 50, false, false, false));

        // Family 1 (S2 heavy user, S3 light user)
        usageRecordRepo.save(new UsageRecord(sub2.getId(), familyId1, LocalDateTime.now().minusDays(5), 20000, 100, 100, false, false, false));
        usageRecordRepo.save(new UsageRecord(sub3.getId(), familyId1, LocalDateTime.now().minusDays(3), 6000, 50, 50, true, false, true)); // roaming + extra SMS

        // Premium (very high usage, but still under huge allowance)
        usageRecordRepo.save(new UsageRecord(sub4.getId(), null, LocalDateTime.now().minusDays(2), 40000, 1000, 500, false, true, false));

        // Student plan (slightly over usage)
        usageRecordRepo.save(new UsageRecord(sub5.getId(), null, LocalDateTime.now().minusDays(1), 3500, 120, 80, false, false, false));

        // Family 2 (S6 hogging, S7 under-using → fairness check)
        usageRecordRepo.save(new UsageRecord(sub6.getId(), familyId2, LocalDateTime.now().minusDays(6), 18000, 300, 200, false, false, false));
        usageRecordRepo.save(new UsageRecord(sub7.getId(), familyId2, LocalDateTime.now().minusDays(4), 3000, 50, 30, false, false, false));

        // --- Extra scenarios ---
        // Night usage (free nights or discounted nights scenario)
        usageRecordRepo.save(new UsageRecord(sub1.getId(), null, LocalDateTime.now().withHour(23).minusDays(7), 2000, 30, 10, false, false, false));

        // Weekend heavy usage (Saturday evening binge streaming)
        usageRecordRepo.save(new UsageRecord(sub2.getId(), familyId1, LocalDateTime.now().withHour(20).minusWeeks(2),
                8000, 200, 50, false, false, false));

        // Extreme Overage (huge over data, voice, SMS beyond plan)
        usageRecordRepo.save(new UsageRecord(sub5.getId(), null, LocalDateTime.now().minusDays(2), 8000, 500, 300, false, false, true)); // hitting SMS overage

        // Night roaming (travel abroad, late night usage)
        usageRecordRepo.save(new UsageRecord(sub3.getId(), familyId1, LocalDateTime.now().withHour(1).minusDays(5),
                1000, 20, 5, true, false, false));

        // Weekend + roaming combo
        usageRecordRepo.save(new UsageRecord(sub4.getId(), null, LocalDateTime.now().withHour(15).minusWeeks(1),
                5000, 200, 100, true, true, false));




        // Iterate through all subscriptions
        for (Subscription sub : subscriptionRepo.findAll()) {
            Plan plan = planRepo.findById(sub.getPlanId()).orElseThrow(()->new PlanNotFoundException("Plan not Found"));

            // Member usages = usage records for this subscription
            List<UsageRecord> memberUsage = usageRecordRepo.findBySubscriptionId(sub.getId());

            // All usages = if family, include all family member usages; else just self
            List<UsageRecord> allUsage;
            if (plan.isFamilyPlan() && sub.getFamilyId() != null) {
                allUsage = usageRecordRepo.findByFamilyId(sub.getFamilyId());
            } else {
                allUsage = memberUsage;
            }

            // Generate invoice
            billingService.generateInvoice(sub, plan, memberUsage, allUsage);
        }

        System.out.println("Bootstrap completed.");

    }

}