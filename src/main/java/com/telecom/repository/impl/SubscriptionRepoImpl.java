package com.telecom.repository.impl;

import com.telecom.models.Subscription;
import com.telecom.repository.interfaces.SubscriptionRepo;

import java.util.*;

public class SubscriptionRepoImpl implements SubscriptionRepo {
    private final Map<String, Subscription> db = new HashMap<>();

    @Override
    public void save(Subscription subscription) {
        db.put(Subscription.getId(), subscription);
    }

    @Override
    public Optional<Subscription> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Subscription> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public List<Subscription> findByCustomerId(String customerId) {
        List<Subscription> result = new ArrayList<>();
        for (Subscription sub : db.values()) {
            if (sub.getCustomerId().equals(customerId)) {
                result.add(sub);
            }
        }
        return result;
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }
}
