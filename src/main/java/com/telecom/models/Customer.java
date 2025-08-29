package com.telecom.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Customer {
    private Long id;
    private String name;
    private String email;
    private Long referredBy;
    private boolean creditBlocked;


}