package com.telecom.models;

import lombok.*;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private String id;
    private String subscriptionId;
    private YearMonth billingCycle;

    private double baseRental;
    private double overageCharges;
    private double roamingCharges;
    private double discounts;
    private double taxes;
    private double totalAmount;

    private boolean paid;

    public Invoice() {
    }

    public Invoice(String id, String subscriptionId, YearMonth billingCycle, double baseRental, double overageCharges, double roamingCharges, double discounts, double taxes, double totalAmount, boolean paid) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.billingCycle = billingCycle;
        this.baseRental = baseRental;
        this.overageCharges = overageCharges;
        this.roamingCharges = roamingCharges;
        this.discounts = discounts;
        this.taxes = taxes;
        this.totalAmount = totalAmount;
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public YearMonth getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(YearMonth billingCycle) {
        this.billingCycle = billingCycle;
    }

    public double getBaseRental() {
        return baseRental;
    }

    public void setBaseRental(double baseRental) {
        this.baseRental = baseRental;
    }

    public double getOverageCharges() {
        return overageCharges;
    }

    public void setOverageCharges(double overageCharges) {
        this.overageCharges = overageCharges;
    }

    public double getRoamingCharges() {
        return roamingCharges;
    }

    public void setRoamingCharges(double roamingCharges) {
        this.roamingCharges = roamingCharges;
    }

    public double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(double discounts) {
        this.discounts = discounts;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
