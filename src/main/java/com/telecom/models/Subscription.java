package com.telecom.models;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private String id;
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
}
