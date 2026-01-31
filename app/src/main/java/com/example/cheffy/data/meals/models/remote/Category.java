package com.example.cheffy.data.meals.models.remote;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("idCategory")
    private String idCategory;

    @SerializedName("strCategory")
    private String name;

    @SerializedName("strCategoryThumb")
    private String thumbnail;

    @SerializedName("strCategoryDescription")
    private String description;

    public Category() {}

    public Category(String idCategory, String name, String thumbnail, String description) {
        this.idCategory = idCategory;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }
}
