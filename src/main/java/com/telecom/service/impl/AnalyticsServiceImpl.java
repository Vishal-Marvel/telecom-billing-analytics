package com.telecom.service.impl;




import com.telecom.models.*;
import com.telecom.repository.interfaces.*;
import com.telecom.service.interfaces.AnalyticsService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsServiceImpl implements AnalyticsService {

    @Override
    public List<Customer> topDataUsers(List<UsageRecord> usage, int n) {
        return usage.stream()
                .collect(Collectors.groupingBy(UsageRecord::getSubscriptionId,
                        Collectors.summingDouble(UsageRecord::getData)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .map(e -> new Customer(e.getKey(), "User-" + e.getKey(), "demo@mail.com", null, false))
                .toList();
    }

    @Override
    public Map<String, Double> arpuByPlan(List<Invoice> invoices) {
        return invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getSubscriptionId,
                        Collectors.averagingDouble(Invoice::getTotalAmount)));
    }

    @Override
    public Map<String, Object> overageDistribution(List<Invoice> invoices) {
        DoubleSummaryStatistics stats = invoices.stream()
                .collect(Collectors.summarizingDouble(Invoice::getOverageCharges));
        return Map.of(
                "count", stats.getCount(),
                "total", stats.getSum(),
                "average", stats.getAverage()
        );
    }

    @Override
    public List<Customer> detectCreditRisk(List<Invoice> invoices, List<Customer> customers) {
        // simple rule: unpaid invoices => credit risk
        Set<String> riskySubs = invoices.stream()
                .filter(inv -> !inv.isPaid())
                .map(Invoice::getSubscriptionId)
                .collect(Collectors.toSet());
        return customers.stream()
                .filter(c -> riskySubs.contains(c.getId()))
                .toList();
    }

    @Override
    public Map<String, String> recommendPlans(List<Invoice> invoices, List<Plan> plans) {
        // simple recommendation: if overage > threshold, suggest higher rental plan
        Map<String, String> recommendations = new HashMap<>();
        invoices.forEach(inv -> {
            if (inv.getOverageCharges() > 100) {
                Plan higher = plans.stream().max(Comparator.comparingDouble(Plan::getMonthlyRental)).orElse(null);
                if (higher != null) recommendations.put(inv.getSubscriptionId(), higher.getName());
            }
        });
        return recommendations;
    }
}