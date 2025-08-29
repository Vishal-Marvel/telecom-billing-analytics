package com.telecom.models;

import com.telecom.models.enums.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private String username;
    private String password;
    private Role role;
    private String customerId;
}
