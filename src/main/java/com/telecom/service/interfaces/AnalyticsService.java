package com.telecom.service.interfaces;

import com.telecom.models.Customer;
import com.telecom.models.Plan;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {

    List<Customer> getTopNDataUsers(LocalDate start, LocalDate end, int n);

    Map<Long, Double> getARPUByPlan(LocalDate start, LocalDate end);

    Map<Long, OverageStats> getOverageDistribution(LocalDate start, LocalDate end);

    List<Customer> getCreditRiskCustomers();

    Map<Long, Plan> recommendHigherPlans(int overageThreshold, int months);

    class OverageStats {
        public final int count;
        public final double total;
        public final double average;
        public OverageStats(int count, double total, double average) {
            this.count = count;
            this.total = total;
            this.average = average;
        }
    }
}