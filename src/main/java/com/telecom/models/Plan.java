package com.telecom.models;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    private String id;
    private String name;
    private double monthlyRental;
    private int validity;

    // Allowances
    private double dataAllowanceMb;
    private int voiceAllowanceMin;
    private int smsAllowance;

    // Overage rates
    private double dataOverageRate;    // per MB
    private double voiceOverageRate;   // per min
    private double smsOverageRate;     // per SMS

    // Special rules
    private boolean weekendFreeVoice;
    private boolean rolloverAllowed;
    private boolean isFamilyPlan;
    private double familyShareCap;// % of pooled data a single member can use
}
