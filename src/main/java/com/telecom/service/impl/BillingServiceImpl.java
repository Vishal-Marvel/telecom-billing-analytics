package com.telecom.service.impl;

import com.telecom.models.*;
import com.telecom.service.interfaces.BillingService;
import com.telecom.utils.TaxUtils;

import java.time.DayOfWeek;
import java.util.List;

public class BillingServiceImpl implements BillingService {

    @Override
    public Invoice generateInvoice(Subscription subscription, Plan plan, List<UsageRecord> usageRecords) {
        double baseRental = plan.getMonthlyRental();
        double usageCharges = 0.0;
        double discounts = 0.0;

        for (UsageRecord record : usageRecords) {
            double recordCharge = rateUsage(record, plan);

            // Apply night discount if applicable
            recordCharge = applyNightDiscount(recordCharge, record);

            usageCharges += recordCharge;
        }

        // Taxes
        double taxes = TaxUtils.applyGST(baseRental + usageCharges);

        Invoice invoice = new Invoice();
        invoice.setSubscriptionId(subscription.getId());   // use instance, not class
        invoice.setBaseRental(baseRental);
        invoice.setOverageCharges(usageCharges);
        invoice.setDiscounts(discounts);
        invoice.setTaxes(taxes);
        invoice.setTotalAmount(baseRental + usageCharges + taxes - discounts);
        invoice.setPaid(false);

        return invoice;

    }

    @Override
    public double rateUsage(UsageRecord usage, Plan plan) {
        double total = 0.0;

        // DATA
        if (usage.getData() > plan.getDataAllowanceMb()) {
            double extraData = usage.getData() - plan.getDataAllowanceMb();
            total += extraData * plan.getDataOverageRate();
        }

        // VOICE
        total += calculateVoiceCharge(usage, plan);

        // SMS
        if (usage.getSmsCount() > plan.getSmsAllowance()) {
            int extraSms = usage.getSmsCount() - plan.getSmsAllowance();
            total += extraSms * plan.getSmsOverageRate();
        }

        // Roaming / International surcharge
        if (usage.isRoaming()) {
            total *= 1.5;
        }
        if (usage.isInternational()) {
            total *= 3.0;
        }

        return total;
    }

    @Override
    public double calculateVoiceCharge(UsageRecord usage, Plan plan) {
        int usedMinutes = usage.getVoiceMinutes();

        // Weekend free minutes
        boolean isWeekend = usage.getTimestamp().getDayOfWeek() == DayOfWeek.SATURDAY ||
                usage.getTimestamp().getDayOfWeek() == DayOfWeek.SUNDAY;

        if (plan.isWeekendFreeVoice() && isWeekend) {
            return 0.0;
        }

        // Normal allowance check
        if (usedMinutes > plan.getVoiceAllowanceMin()) {
            int extraMinutes = usedMinutes - plan.getVoiceAllowanceMin();
            return extraMinutes * plan.getVoiceOverageRate();
        }
        return 0.0;
    }

    @Override
    public double applyNightDiscount(double amount, UsageRecord usage) {
        if (usage.isNightTime()) {
            return amount * 0.5;
        }
        return amount;
    }
}

