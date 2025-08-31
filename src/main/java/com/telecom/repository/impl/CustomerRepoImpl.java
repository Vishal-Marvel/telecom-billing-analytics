package com.telecom.repository.impl;

import com.telecom.models.Customer;
import com.telecom.repository.interfaces.CustomerRepo;

import java.util.*;

public class CustomerRepoImpl implements CustomerRepo {
    private Map<String, Customer> db = new HashMap<>();

    @Override
    public void save(Customer customer) {
        db.put(customer.getId(), customer);
    }

    @Override
    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(db.get(customerId));
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }
}
