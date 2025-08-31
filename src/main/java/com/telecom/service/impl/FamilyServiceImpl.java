package com.telecom.service.impl;

import com.telecom.models.Family;
import com.telecom.repository.interfaces.FamilyRepo;
import com.telecom.service.interfaces.FamilyService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService {
    private final FamilyRepo familyRepo;

    @Override
    public Family addFamilyMember(String familyId, String customerId) {
        Family family = familyRepo.findByFamilyId(familyId).orElse(new Family(familyId, new ArrayList<>()));
        family.getCustomerIds().add(customerId);
        return familyRepo.save(family);
    }
}
