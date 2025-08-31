package com.telecom.service.interfaces;

import com.telecom.models.Plan;

import java.util.List;

public interface PlanService {
    void addPlan(Plan plan);
    Plan getPlan(String id);
    List<Plan> getAllPlans();
}
