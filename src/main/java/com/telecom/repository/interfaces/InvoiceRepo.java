package com.telecom.repository.interfaces;

import com.telecom.models.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepo {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(String id);
    List<Invoice> findAll();
    List<Invoice> findBySubscriptionId(String subscriptionId);
    void delete(String id);
    Invoice update(Invoice invoice);
}
