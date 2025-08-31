package com.telecom.repository.impl;

import com.telecom.models.UsageRecord;
import com.telecom.repository.interfaces.UsageRecordRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsageRecordRepoImpl implements UsageRecordRepo {

    private final List<UsageRecord> records = new ArrayList<>();

    @Override
    public void save(UsageRecord record) {
        records.add(record);
    }

    @Override
    public UsageRecord findById(String subscriptionId, LocalDateTime timestamp) {
        return records.stream()
                .filter(r -> r.getSubscriptionId().equals(subscriptionId) && r.getTimestamp().equals(timestamp))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UsageRecord> findBySubscriptionId(String subscriptionId) {
        return records.stream()
                .filter(r -> r.getSubscriptionId().equals(subscriptionId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsageRecord> findByDateRange(LocalDate start, LocalDate end) {
        return records.stream()
                .filter(r -> {
                    LocalDate date = r.getTimestamp().toLocalDate();
                    return (date.isEqual(start) || date.isAfter(start)) &&
                            (date.isEqual(end) || date.isBefore(end));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UsageRecord> findByDateAfter(LocalDate since) {
        return records.stream()
                .filter(r -> r.getTimestamp().toLocalDate().isAfter(since) || r.getTimestamp().toLocalDate().isEqual(since))
                .collect(Collectors.toList());
    }

    @Override
    public List<UsageRecord> findAll() {
        return new ArrayList<>(records);
    }

    @Override
    public void update(UsageRecord record) {
        for (int i = 0; i < records.size(); i++) {
            UsageRecord r = records.get(i);
            if (r.getSubscriptionId().equals(record.getSubscriptionId()) &&
                    r.getTimestamp().equals(record.getTimestamp())) {
                records.set(i, record);
                return;
            }
        }
    }

    @Override
    public void delete(String subscriptionId, LocalDateTime timestamp) {
        records.removeIf(r -> r.getSubscriptionId().equals(subscriptionId) && r.getTimestamp().equals(timestamp));
    }
}
