package com.telecom.service.impl;

import com.telecom.models.Subscription;
import com.telecom.repository.interfaces.SubscriptionRepo;
import com.telecom.service.interfaces.SubscriptionService;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepo repo;

    public SubscriptionServiceImpl(SubscriptionRepo repo) {
        this.repo = repo;
    }

    @Override
    public void addSubscription(Subscription subscription) {
        repo.save(subscription);
    }

    @Override
    public Subscription getSubscription(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Subscription not found"));
    }

    @Override
    public List<Subscription> getCustomerSubscriptions(String customerId) {
        return repo.findByCustomerId(customerId);
    }

    @Override
    public void requestMNP(String subscriptionId, String targetOperator) {
        Subscription sub = getSubscription(subscriptionId);
        if (sub.isMnpPending()) throw new RuntimeException("MNP already pending.");
        sub.setMnpPending(true);
        sub.setMnpRequestDate(LocalDate.now());
        sub.setTargetOperator(targetOperator);
        repo.save(sub);
    }

    @Override
    public void completeMNP(String subscriptionId) {
        Subscription sub = getSubscription(subscriptionId);
        if (!sub.isMnpPending()) throw new RuntimeException("No MNP pending.");
        sub.setMnpPending(false);
        sub.setEndDate(LocalDate.now());
        repo.save(sub);
    }

    @Override
    public void cancelMNP(String subscriptionId) {
        Subscription sub = getSubscription(subscriptionId);
        sub.setMnpPending(false);
        sub.setTargetOperator(null);
        repo.save(sub);
    }

    @Override
    public void changePlan(String subscriptionId, String newPlanId) {
        Subscription sub = getSubscription(subscriptionId);
        if (sub.isMnpPending()) throw new RuntimeException("Cannot change plan during MNP.");
        sub.setPlanId(newPlanId);
        repo.save(sub);
    }
}
