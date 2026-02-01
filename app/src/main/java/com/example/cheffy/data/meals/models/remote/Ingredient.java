package com.example.cheffy.data.meals.models.remote;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("idIngredient")
    private final String idIngredient;

    @SerializedName("strIngredient")
    private final String name;

    @SerializedName("strDescription")
    private final String description;

    @SerializedName("strType")
    private final String type;

    public Ingredient(String idIngredient, String name, String description, String type) {
        this.idIngredient = idIngredient;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getThumbnail() {
        if (name == null || name.isEmpty()) return null;
        return "https://www.themealdb.com/images/ingredients/" + name + "-Small.png";
    }
}
