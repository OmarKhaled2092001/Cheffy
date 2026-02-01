package com.example.cheffy.ui.plan.model;

import com.example.cheffy.data.meals.models.remote.RemoteMeal;

public class PlannedMeal {
    private final long planId;
    private final RemoteMeal meal;
    private final String dayOfWeek;

    public PlannedMeal(long planId, RemoteMeal meal, String dayOfWeek) {
        this.planId = planId;
        this.meal = meal;
        this.dayOfWeek = dayOfWeek;
    }

    public long getPlanId() {
        return planId;
    }

    public RemoteMeal getMeal() {
        return meal;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getIdMeal() {
        return meal != null ? meal.getIdMeal() : null;
    }

    public String getName() {
        return meal != null ? meal.getName() : null;
    }

    public String getThumbnail() {
        return meal != null ? meal.getThumbnail() : null;
    }

    public String getCategory() {
        return meal != null ? meal.getCategory() : null;
    }

    public String getArea() {
        return meal != null ? meal.getArea() : null;
    }
}
