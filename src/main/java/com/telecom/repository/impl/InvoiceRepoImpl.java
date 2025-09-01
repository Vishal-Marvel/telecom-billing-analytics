package com.telecom.repository.impl;

import com.telecom.models.Invoice;
import com.telecom.repository.interfaces.InvoiceRepo;

import java.util.*;
import java.util.stream.Collectors;

public class InvoiceRepoImpl implements InvoiceRepo {

    private final Map<String, Invoice> db = new HashMap<>();

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice.getId() == null) {
            invoice.setId("I"+UUID.randomUUID().toString().substring(0,3)); // Generate an ID if one doesn't exist
        }
        db.put(invoice.getId(), invoice);
        return invoice;
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Invoice> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public List<Invoice> findBySubscriptionId(String subscriptionId) {
        return db.values().stream()
                .filter(invoice -> subscriptionId.equals(invoice.getSubscriptionId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }

    @Override
    public Invoice update(Invoice invoice) {
        db.put(invoice.getId(), invoice);
        return invoice;
    }
}