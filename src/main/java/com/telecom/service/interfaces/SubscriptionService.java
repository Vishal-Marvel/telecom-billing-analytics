package com.telecom.service.interfaces;

import java.util.List;

import com.telecom.models.Subscription;


public interface SubscriptionService {
    void addSubscription(Subscription subscription);
    Subscription getSubscription(String id);
    List<Subscription> getCustomerSubscriptions(String customerId);

    void requestMNP(String subscriptionId, String targetOperator);
    void completeMNP(String subscriptionId);
    void cancelMNP(String subscriptionId);
    void changePlan(String subscriptionId, String newPlanId);
}
