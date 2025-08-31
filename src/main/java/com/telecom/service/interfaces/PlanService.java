package com.telecom.service.interfaces;

import com.telecom.models.Plan;

import java.util.List;

public interface PlanService {
    Plan addPlan(Plan plan);
    Plan getPlan(String id);
    List<Plan> getAllPlans();
    Plan updatePlan(Plan plan);
    void deletePlan(String id);
}
