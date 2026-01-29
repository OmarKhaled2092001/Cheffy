package com.example.cheffy.data.meals.models;


public enum SearchType {
    CATEGORY("category"),
    AREA("area"),
    INGREDIENT("ingredient");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayTitle(String filterValue) {
        switch (this) {
            case CATEGORY:
                return filterValue + " Recipes";
            case AREA:
                return filterValue + " Cuisine";
            case INGREDIENT:
                return "Recipes with " + filterValue;
            default:
                return filterValue;
        }
    }

    public String getSubtitle() {
        switch (this) {
            case CATEGORY:
                return "Explore delicious recipes";
            case AREA:
                return "Discover regional flavors";
            case INGREDIENT:
                return "Find the perfect dish";
            default:
                return "Browse recipes";
        }
    }
}
