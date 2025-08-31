package com.telecom.models;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Customer {
    private String id;
    private String name;
    private String email;
    private String referredBy;    
    private boolean creditBlocked;

}