package com.telecom.service.impl;

import com.telecom.models.Plan;
import com.telecom.repository.interfaces.PlanRepo;
import com.telecom.service.interfaces.PlanService;

import java.util.List;

public class PlanServiceImpl implements PlanService {
    private final PlanRepo repo;

    public PlanServiceImpl(PlanRepo repo) {
        this.repo = repo;
    }

    @Override
    public void addPlan(Plan plan) {
        repo.save(plan);
    }

    @Override
    public Plan getPlan(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    @Override
    public List<Plan> getAllPlans() {
        return repo.findAll();
    }
}
