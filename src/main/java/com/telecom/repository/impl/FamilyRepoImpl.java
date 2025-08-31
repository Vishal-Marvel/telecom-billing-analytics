package com.telecom.repository.impl;

import com.telecom.models.Family;
import com.telecom.repository.interfaces.FamilyRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FamilyRepoImpl implements FamilyRepo {
    private final Map<String, Family> db = new HashMap<>();

    @Override
    public Family save(Family family) {
        db.put(family.getFamilyId(), family);
        return family;
    }

    @Override
    public Optional<Family> findByFamilyId(String familyId) {
        return Optional.ofNullable(db.get(familyId));
    }
}
