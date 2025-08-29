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
    private boolean nightTime;   // true if between 00:00–06:00
}