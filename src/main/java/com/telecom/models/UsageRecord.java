package com.telecom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class UsageRecord {
    private String subscriptionId;
    private LocalDateTime timestamp;
    private double data;
    private int voiceMinutes;
    private int smsCount;

    private boolean roaming;
    private boolean international;
    private boolean nightTime;// true if between 00:00–06:00

    public UsageRecord() {
    }

    public UsageRecord(String subscriptionId, LocalDateTime timestamp, double data, int voiceMinutes, int smsCount, boolean roaming, boolean international, boolean nightTime) {
        this.subscriptionId = subscriptionId;
        this.timestamp = timestamp;
        this.data = data;
        this.voiceMinutes = voiceMinutes;
        this.smsCount = smsCount;
        this.roaming = roaming;
        this.international = international;
        this.nightTime = nightTime;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public int getVoiceMinutes() {
        return voiceMinutes;
    }

    public void setVoiceMinutes(int voiceMinutes) {
        this.voiceMinutes = voiceMinutes;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public boolean isNightTime() {
        return nightTime;
    }

    public void setNightTime(boolean nightTime) {
        this.nightTime = nightTime;
    }

    public boolean isInternational() {
        return international;
    }

    public void setInternational(boolean international) {
        this.international = international;
    }
}