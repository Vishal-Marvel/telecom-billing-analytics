package com.telecom.service.interfaces;

import com.telecom.models.Customer;
import com.telecom.models.Invoice;
import com.telecom.models.Plan;
import com.telecom.models.UsageRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {

    List<Customer> topDataUsers(List<UsageRecord> usage, int n);
    Map<String, Double> arpuByPlan(List<Invoice> invoices);
    Map<String, Object> overageDistribution(List<Invoice> invoices);
    List<Customer> detectCreditRisk(List<Invoice> invoices, List<Customer> customers);
    Map<String, String> recommendPlans(List<Invoice> invoices, List<Plan> plans);
}