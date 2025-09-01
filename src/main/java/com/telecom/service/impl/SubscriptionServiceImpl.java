package com.telecom.service.impl;

import com.telecom.exceptions.SubscriptionException;
import com.telecom.models.Family;
import com.telecom.models.Subscription;
import com.telecom.repository.interfaces.SubscriptionRepo;
import com.telecom.service.interfaces.FamilyService;
import com.telecom.service.interfaces.SubscriptionService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepo repo;
    private final FamilyService familyService;


    @Override
    public void addSubscription(Subscription subscription) throws SubscriptionException {
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
    public List<Subscription> getSubscriptionWithMNP() {
        return repo.findSubscriptionsWithMNP();
    }

    @Override
    public void requestMNP(String subscriptionId, String targetOperator) {
        Subscription sub = getSubscription(subscriptionId);
        if (sub.isMnpPending()) throw new RuntimeException("MNP already pending.");
        sub.setMnpPending(true);
        sub.setMnpRequestDate(LocalDate.now());
        sub.setTargetOperator(targetOperator);
        repo.update(sub);
    }

    @Override
    public void completeMNP(String subscriptionId) {
        Subscription sub = getSubscription(subscriptionId);
        if (!sub.isMnpPending()) throw new RuntimeException("No MNP pending.");
        sub.setMnpPending(false);
        sub.setEndDate(LocalDate.now());
        repo.update(sub);
    }

    @Override
    public void cancelMNP(String subscriptionId) {
        Subscription sub = getSubscription(subscriptionId);
        sub.setMnpPending(false);
        sub.setTargetOperator(null);
        repo.update(sub);
    }

    @Override
    public void changePlan(String subscriptionId, String newPlanId) {
        Subscription sub = getSubscription(subscriptionId);
        if (sub.isMnpPending()) throw new RuntimeException("Cannot change plan during MNP.");
        sub.setPlanId(newPlanId);
        String familyId = UUID.randomUUID().toString().substring(0, 3);
        familyService.createFamily(Family.builder()
                .familyId("F"+familyId)
                .build());
        familyService.addFamilyMember(familyId, sub.getCustomerId());
        sub.setFamilyId(familyId);
        repo.update(sub);
    }
}
