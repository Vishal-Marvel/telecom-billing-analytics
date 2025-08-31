package com.telecom.service.impl;

import com.telecom.models.*;
import com.telecom.repository.interfaces.InvoiceRepo;
import com.telecom.service.interfaces.BillingService;
import com.telecom.utils.TaxUtils;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepo invoiceRepo;

    private static final double FAMILY_FAIRNESS_SURCHARGE = 50.0;
    private static final double FAMILY_SHARE_CAP_THRESHOLD = 0.6; // 60%

    @Override
    public Invoice generateInvoice(Subscription subscription, Plan plan, List<UsageRecord> memberUsage, List<UsageRecord> allUsage) {
        if (plan.isFamilyPlan()) {
            return generateFamilyInvoice(subscription, plan, memberUsage, allUsage);
        } else {
            return generateIndividualInvoice(subscription, plan, memberUsage);
        }
    }

    private Invoice generateIndividualInvoice(Subscription subscription, Plan plan, List<UsageRecord> usageRecords) {
        double baseRental = plan.getMonthlyRental();
        double usageCharges = 0.0;
        double discounts = 0.0;

        for (UsageRecord record : usageRecords) {
            double recordCharge = rateUsage(record, plan);
            recordCharge = applyNightDiscount(recordCharge, record);
            usageCharges += recordCharge;
        }

        double taxes = TaxUtils.applyGST(baseRental + usageCharges);

        Invoice invoice = new Invoice();
        invoice.setSubscriptionId(subscription.getId());
        invoice.setBaseRental(baseRental);
        invoice.setOverageCharges(usageCharges);
        invoice.setDiscounts(discounts);
        invoice.setTaxes(taxes);
        invoice.setTotalAmount(baseRental + usageCharges + taxes - discounts);
        invoice.setPaid(false);

        return invoiceRepo.save(invoice);
    }

    private Invoice generateFamilyInvoice(Subscription subscription, Plan plan, List<UsageRecord> memberUsage, List<UsageRecord> allUsage) {
        double baseRental = plan.getMonthlyRental();
        double overageCharges = 0.0;
        double fairnessSurcharge = 0.0;

        // --- Data Overage Calculation (Pooled) ---
        List<UsageRecord> familyUsage = allUsage.stream()
                .filter(u -> subscription.getFamilyId().equals(u.getFamilyId()))
                .collect(Collectors.toList());

        double totalFamilyDataUsage = familyUsage.stream().mapToDouble(UsageRecord::getData).sum();
        double familyDataAllowance = plan.getDataAllowanceMb();

        if (totalFamilyDataUsage > familyDataAllowance) {
            double overageData = totalFamilyDataUsage - familyDataAllowance;
            // Distribute overage charge based on individual's contribution to total usage
            double memberDataUsage = memberUsage.stream().mapToDouble(UsageRecord::getData).sum();
            double memberContributionRatio = memberDataUsage / totalFamilyDataUsage;
            overageCharges += (overageData * plan.getDataOverageRate()) * memberContributionRatio;
        }

        // --- Family Fairness Surcharge ---
        double memberDataUsage = memberUsage.stream().mapToDouble(UsageRecord::getData).sum();
        if (memberDataUsage > (familyDataAllowance * FAMILY_SHARE_CAP_THRESHOLD)) {
            fairnessSurcharge = FAMILY_FAIRNESS_SURCHARGE;
        }

        // --- Voice and SMS Overage (Individual) ---
        double voiceAndSmsOverage = 0.0;
        for (UsageRecord record : memberUsage) {
            // Don't double-count data charges, just calculate voice/sms
            voiceAndSmsOverage += calculateVoiceCharge(record, plan);
            if (record.getSmsCount() > plan.getSmsAllowance()) {
                int extraSms = record.getSmsCount() - plan.getSmsAllowance();
                voiceAndSmsOverage += extraSms * plan.getSmsOverageRate();
            }
        }

        double totalCharges = baseRental + overageCharges + fairnessSurcharge + voiceAndSmsOverage;
        double taxes = TaxUtils.applyGST(totalCharges);

        Invoice invoice = new Invoice();
        invoice.setSubscriptionId(subscription.getId());
        invoice.setBaseRental(baseRental);
        invoice.setOverageCharges(overageCharges + voiceAndSmsOverage);
        // Add fairness surcharge to discounts field for now for visibility
        invoice.setDiscounts(-fairnessSurcharge); // Negative discount is a charge
        invoice.setTaxes(taxes);
        invoice.setTotalAmount(totalCharges + taxes);
        invoice.setPaid(false);

        return invoiceRepo.save(invoice);
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

    @Override
    public void payInvoice(String invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setPaid(true);
        invoiceRepo.update(invoice);
    }
}
