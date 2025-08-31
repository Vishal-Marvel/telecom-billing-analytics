package com.telecom.models;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private static String id;
    private String customerId;
    private String phoneNumber;
    private String planId;
    private LocalDate startDate;
    private LocalDate endDate;

    // Family group support
    private String familyId;

    // MNP handling
    private boolean mnpPending;
    private LocalDate mnpRequestDate;
    private String targetOperator;
    public Subscription() {
    }

    public Subscription(String id, String customerId, String phoneNumber, String planId, LocalDate startDate, LocalDate endDate, String familyId, boolean mnpPending, LocalDate mnpRequestDate, String targetOperator) {
        this.id = id;
        this.customerId = customerId;
        this.phoneNumber = phoneNumber;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.familyId = familyId;
        this.mnpPending = mnpPending;
        this.mnpRequestDate = mnpRequestDate;
        this.targetOperator = targetOperator;
    }

    public static String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public boolean isMnpPending() {
        return mnpPending;
    }

    public void setMnpPending(boolean mnpPending) {
        this.mnpPending = mnpPending;
    }

    public LocalDate getMnpRequestDate() {
        return mnpRequestDate;
    }

    public void setMnpRequestDate(LocalDate mnpRequestDate) {
        this.mnpRequestDate = mnpRequestDate;
    }

    public String getTargetOperator() {
        return targetOperator;
    }

    public void setTargetOperator(String targetOperator) {
        this.targetOperator = targetOperator;
    }
}
