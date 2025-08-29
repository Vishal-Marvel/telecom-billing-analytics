package com.telecom.bootstrap;

import com.telecom.models.User;
import com.telecom.models.enums.Role;
import com.telecom.repository.interfaces.UserRepo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BootstrapEngine {
    private final UserRepo userRepo;

    public void run() {
        System.out.println("Bootstrapping...");
        userRepo.createUser(User.builder().username("admin").password("admin").role(Role.ADMIN).build());
        System.out.println("Bootstrap completed");
    }
}
