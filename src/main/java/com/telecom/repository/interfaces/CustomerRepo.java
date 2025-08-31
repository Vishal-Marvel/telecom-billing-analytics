package com.telecom.repository.interfaces;

import com.telecom.models.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo {
    void save(Customer customer);
    Optional<Customer> findById(String customerId);
    List<Customer> findAll();
    void delete(String id);
}
