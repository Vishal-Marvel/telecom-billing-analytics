package com.telecom.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Plan {
    private Long id;
    private String name;
    private double monthlyRental;
    private double dataAllowanceGb;
    private double voiceAllowanceMinutes;
    private int smsAllowanceCount;
    private boolean isFamilyShare;
    private double overageRateGb = 10.0;
    private double overageRateVoice = 0.5;
    private double overageRateSms = 0.2;
    private boolean weekendFreeVoice;
    private double familyShareCap = 0.60;
}
