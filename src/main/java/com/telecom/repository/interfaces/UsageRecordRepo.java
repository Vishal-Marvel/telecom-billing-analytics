package com.telecom.repository.interfaces;

import com.telecom.models.UsageRecord;

import java.time.LocalDate;
import java.util.List;

public interface UsageRecordRepo {
    void save(UsageRecord record);

    UsageRecord findById(String subscriptionId, java.time.LocalDateTime timestamp);

    List<UsageRecord> findBySubscriptionId(String subscriptionId);

    List<UsageRecord> findByDateRange(LocalDate start, LocalDate end);

    List<UsageRecord> findByDateAfter(LocalDate since);

    List<UsageRecord> findAll();

    void update(UsageRecord record);

    void delete(String subscriptionId, java.time.LocalDateTime timestamp);
}
