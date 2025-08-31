package com.telecom.repository.interfaces;

import com.telecom.models.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanRepo {
    void save(Plan plan);
    Optional<Plan> findById(String id);
    List<Plan> findAll();
    void delete(String id);
}
