package com.telecom.models;

import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Customer {
    private String id;
    private String name;
    private String email;
    private String referredBy;    // Customer ID of referrer
    private boolean creditBlocked;

    public Customer() {
    }

    public Customer(String id, String name, String email, String referredBy, boolean creditBlocked) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.referredBy = referredBy;
        this.creditBlocked = creditBlocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public boolean isCreditBlocked() {
        return creditBlocked;
    }

    public void setCreditBlocked(boolean creditBlocked) {
        this.creditBlocked = creditBlocked;
    }
}