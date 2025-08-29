package com.telecom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class UsageRecord {
    private Long customerId;
    private String usageType; // e.g., "call", "sms", "data"
    private Double amount; // e.g., duration in minutes for calls, number of messages for SMS, data in MB for data usage
    private String timestamp; // e.g., "2023-10-01T10:15:30"


}