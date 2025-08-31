package com.telecom.service.interfaces;

import com.telecom.models.Family;

public interface FamilyService {
    Family addFamilyMember(String familyId, String customerId);
}
