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
    private double familyShareCap;// % of pooled data a single member can use

    public Plan() {
    }

    public Plan(String id, String name, double monthlyRental, int validity, double dataAllowanceMb, int voiceAllowanceMin, int smsAllowance, double dataOverageRate, double voiceOverageRate, double smsOverageRate, double familyShareCap, boolean rolloverAllowed, boolean weekendFreeVoice) {
        this.id = id;
        this.name = name;
        this.monthlyRental = monthlyRental;
        this.validity = validity;
        this.dataAllowanceMb = dataAllowanceMb;
        this.voiceAllowanceMin = voiceAllowanceMin;
        this.smsAllowance = smsAllowance;
        this.dataOverageRate = dataOverageRate;
        this.voiceOverageRate = voiceOverageRate;
        this.smsOverageRate = smsOverageRate;
        this.familyShareCap = familyShareCap;
        this.rolloverAllowed = rolloverAllowed;
        this.weekendFreeVoice = weekendFreeVoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMonthlyRental() {
        return monthlyRental;
    }

    public void setMonthlyRental(double monthlyRental) {
        this.monthlyRental = monthlyRental;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public double getDataAllowanceMb() {
        return dataAllowanceMb;
    }

    public void setDataAllowanceMb(double dataAllowanceMb) {
        this.dataAllowanceMb = dataAllowanceMb;
    }

    public int getVoiceAllowanceMin() {
        return voiceAllowanceMin;
    }

    public void setVoiceAllowanceMin(int voiceAllowanceMin) {
        this.voiceAllowanceMin = voiceAllowanceMin;
    }

    public int getSmsAllowance() {
        return smsAllowance;
    }

    public void setSmsAllowance(int smsAllowance) {
        this.smsAllowance = smsAllowance;
    }

    public double getDataOverageRate() {
        return dataOverageRate;
    }

    public void setDataOverageRate(double dataOverageRate) {
        this.dataOverageRate = dataOverageRate;
    }

    public double getVoiceOverageRate() {
        return voiceOverageRate;
    }

    public void setVoiceOverageRate(double voiceOverageRate) {
        this.voiceOverageRate = voiceOverageRate;
    }

    public double getSmsOverageRate() {
        return smsOverageRate;
    }

    public void setSmsOverageRate(double smsOverageRate) {
        this.smsOverageRate = smsOverageRate;
    }

    public boolean isWeekendFreeVoice() {
        return weekendFreeVoice;
    }

    public void setWeekendFreeVoice(boolean weekendFreeVoice) {
        this.weekendFreeVoice = weekendFreeVoice;
    }

    public boolean isRolloverAllowed() {
        return rolloverAllowed;
    }

    public void setRolloverAllowed(boolean rolloverAllowed) {
        this.rolloverAllowed = rolloverAllowed;
    }

    public double getFamilyShareCap() {
        return familyShareCap;
    }

    public void setFamilyShareCap(double familyShareCap) {
        this.familyShareCap = familyShareCap;
    }
}
