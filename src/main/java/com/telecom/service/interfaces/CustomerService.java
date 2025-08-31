package com.telecom.service.interfaces;

import com.telecom.models.Customer;

import java.util.List;

public interface CustomerService {
    void addCustomer(Customer customer);
    Customer getCustomer(String id);
    List<Customer> getAllCustomers();
}
