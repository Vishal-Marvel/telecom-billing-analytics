package com.telecom.models;

import java.util.Objects;

public class Customer {
    private Long id;
    private String name;
    private String email;
    private Long referredBy;
    private boolean creditBlocked;

    public Customer(Long id, String name, String email, Long referredBy, boolean creditBlocked) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.referredBy = referredBy;
        this.creditBlocked = creditBlocked;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getReferredBy() { return referredBy; }
    public void setReferredBy(Long referredBy) { this.referredBy = referredBy; }
    public boolean isCreditBlocked() { return creditBlocked; }
    public void setCreditBlocked(boolean creditBlocked) { this.creditBlocked = creditBlocked; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}