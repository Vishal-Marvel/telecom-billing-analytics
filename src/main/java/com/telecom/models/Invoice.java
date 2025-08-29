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
    private String notes;   // e.g., credit control, fairness surcharge info
}
