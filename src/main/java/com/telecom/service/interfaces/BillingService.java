package com.telecom.service.interfaces;

import com.telecom.models.Invoice;
import com.telecom.models.Plan;
import com.telecom.models.Subscription;
import com.telecom.models.UsageRecord;

import java.util.List;

    public interface BillingService {
        Invoice generateInvoice(Subscription subscription, Plan plan, List<UsageRecord> usageRecords);

        double rateUsage(UsageRecord usage, Plan plan);

        double calculateVoiceCharge(UsageRecord usage, Plan plan);

        double applyNightDiscount(double amount, UsageRecord usage);
    }
