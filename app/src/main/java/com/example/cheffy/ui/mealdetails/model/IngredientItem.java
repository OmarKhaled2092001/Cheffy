package com.example.cheffy.ui.mealdetails.model;

public class IngredientItem {

    private final String name;
    private final String measure;

    public IngredientItem(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    public String getImageUrl() {
        return "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
    }
}
