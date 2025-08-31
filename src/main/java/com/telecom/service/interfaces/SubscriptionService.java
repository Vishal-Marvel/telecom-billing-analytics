package com.telecom.service.interfaces;

import com.telecom.models.Subscription;

import java.util.List;

public interface SubscriptionService {
    void addSubscription(Subscription subscription);
    Subscription getSubscription(String id);
    List<Subscription> getCustomerSubscriptions(String customerId);

    void requestMNP(String subscriptionId, String targetOperator);
    void completeMNP(String subscriptionId);
    void cancelMNP(String subscriptionId);
    void changePlan(String subscriptionId, String newPlanId);
}
