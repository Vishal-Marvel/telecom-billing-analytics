package com.telecom.repository.impl;

import com.telecom.models.Plan;
import com.telecom.repository.interfaces.PlanRepo;

import java.util.*;

public class PlanRepoImpl implements PlanRepo {
    private final Map<String, Plan> db = new HashMap<>();

    @Override
    public Plan save(Plan plan) {
        db.put(plan.getId(), plan);
        return plan;
    }

    @Override
    public Optional<Plan> findById(String id) {
        return  Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Plan> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void delete(String id) {
        db.remove(id);
    }

    @Override
    public Plan update(Plan plan) {
        db.put(plan.getId(), plan);
        return plan;
    }
}
