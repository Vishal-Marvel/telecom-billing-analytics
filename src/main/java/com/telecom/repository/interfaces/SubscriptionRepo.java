package com.telecom.repository.interfaces;

import com.telecom.models.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepo {
    void save(Subscription subscription);
    void update(Subscription subscription);
    Optional<Subscription> findById(String id);
    List<Subscription> findAll();
    List<Subscription> findByCustomerId(String customerId);
    List<Subscription> findSubscriptionsWithMNP();
    void delete(String id);
}
