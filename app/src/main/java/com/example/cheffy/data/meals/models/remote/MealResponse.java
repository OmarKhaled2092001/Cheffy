package com.example.cheffy.data.meals.models.remote;

import java.util.List;

public class MealResponse {
    private final List<RemoteMeal> meals;
    public MealResponse(List<RemoteMeal> meals) {
        this.meals = meals;
    }
    public List<RemoteMeal> getMeals() {
        return meals;
    }
}
