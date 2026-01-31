package com.example.cheffy.data.meals.models.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse {

    @SerializedName("meals")
    private final List<Ingredient> ingredients;

    public IngredientResponse(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
