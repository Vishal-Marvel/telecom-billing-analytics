package com.telecom.service.impl;

import com.telecom.models.Customer;
import com.telecom.repository.interfaces.CustomerRepo;
import com.telecom.service.interfaces.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo repo;

    public CustomerServiceImpl(CustomerRepo repo) {
        this.repo = repo;
    }

    @Override
    public void addCustomer(Customer customer) {
        repo.save(customer);
    }

    @Override
    public Customer getCustomer(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }
}
