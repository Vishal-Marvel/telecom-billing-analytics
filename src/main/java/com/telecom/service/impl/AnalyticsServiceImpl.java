java
        package com.telecom.service.impl;

import com.telecom.models.*;
import com.telecom.repository.interfaces.*;
import com.telecom.service.interfaces.AnalyticsService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsServiceImpl implements AnalyticsService {

    private final UsageRecordRepo usageRecordRepo;
    private final InvoiceRepo invoiceRepo;
    private final CustomerRepo customerRepo;
    private final PlanRepo planRepo;
    private final SubscriptionRepo subscriptionRepo;

    public AnalyticsServiceImpl(
            UsageRecordRepo usageRecordRepo,
            InvoiceRepo invoiceRepo,
            CustomerRepo customerRepo,
            PlanRepo planRepo,
            SubscriptionRepo subscriptionRepo) {
        this.usageRecordRepo = usageRecordRepo;
        this.invoiceRepo = invoiceRepo;
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    // 1. Top N data users in a billing cycle
    @Override
    public List<Customer> getTopNDataUsers(LocalDate start, LocalDate end, int n) {
        // Group by subscriptionId, sum data, map to customerId
        Map<String, Double> dataBySub = usageRecordRepo.findByDateRange(start, end).stream()
                .collect(Collectors.groupingBy(
                        UsageRecord::getSubscriptionId,
                        Collectors.summingDouble(UsageRecord::getData)
                ));

        // Map subscriptionId to customerId, sum if customer has multiple subscriptions
        Map<Long, Double> dataByCustomer = new HashMap<>();
        for (Map.Entry<String, Double> entry : dataBySub.entrySet()) {
            Subscription sub = subscriptionRepo.findById(entry.getKey());
            if (sub != null) {
                dataByCustomer.merge(sub.getCustomerId(), entry.getValue(), Double::sum);
            }
        }

        return dataByCustomer.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(n)
                .map(e -> customerRepo.findById(e.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 2. ARPU (Average Revenue Per User) by plan
    @Override
    public Map<Long, Double> getARPUByPlan(LocalDate start, LocalDate end) {
        List<Invoice> invoices = invoiceRepo.findByDateRange(start, end);
        return invoices.stream()
                .collect(Collectors.groupingBy(
                        Invoice::getPlanId,
                        Collectors.averagingDouble(Invoice::getTotalAmount)
                ));
    }

    // 3. Overage distribution (count, total, average overage per plan)
    @Override
    public Map<Long, OverageStats> getOverageDistribution(LocalDate start, LocalDate end) {
        // This requires overage info, which is not present in UsageRecord.
        // Placeholder: returns empty map.
        return new HashMap<>();
    }

    // 4. Credit risk detection: customers with invoices unpaid > 60 days
    @Override
    public List<Customer> getCreditRiskCustomers() {
        LocalDate now = LocalDate.now();
        return invoiceRepo.findAll().stream()
                .filter(inv -> !inv.isPaid() && ChronoUnit.DAYS.between(inv.getIssueDate(), now) > 60)
                .map(Invoice::getCustomerId)
                .distinct()
                .map(customerRepo::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 5. Plan recommendation engine: suggest higher plans if a subscriber pays frequent overages
    @Override
    public Map<Long, Plan> recommendHigherPlans(int overageThreshold, int months) {
        // This requires overage info, which is not present in UsageRecord.
        // Placeholder: returns empty map.
        return new HashMap<>();
    }
}