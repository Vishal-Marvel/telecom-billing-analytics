package com.telecom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Invoice {
    private Long id;
    private Long subscriptionId;
    private String billingMonth;
    private double totalCharges;
    private double monthlyRental;
    private double overageCharges;
    private double roamingSurcharges;
    private double referralDiscounts;
    private double familyFairnessSurcharge;
    private double tax;
    private boolean isPaid;
    private LocalDate dueDate;
    private int daysUnpaid;
}
