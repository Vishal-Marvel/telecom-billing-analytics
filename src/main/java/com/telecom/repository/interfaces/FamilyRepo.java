package com.telecom.repository.interfaces;

import com.telecom.models.Family;

import java.util.Optional;

public interface FamilyRepo {
    Family save(Family family);
    Optional<Family> findByFamilyId(String familyId);
}
