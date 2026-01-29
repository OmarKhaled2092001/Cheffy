package com.example.cheffy.data.meals.repository;

import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.List;

public interface IMealsRepository {
    void getRandomMeal(MealsDataCallback<RemoteMeal> callback);

    void getCategories(MealsDataCallback<List<Category>> callback);

    void getAreas(MealsDataCallback<List<Area>> callback);

    void getIngredients(MealsDataCallback<List<Ingredient>> callback);

    void getPopularMeals(int count, MealsDataCallback<List<RemoteMeal>> callback);

    void getMealsByFilter(SearchType type, String filter, MealsDataCallback<List<RemoteMeal>> callback);

    void getMealById(String mealId, MealsDataCallback<RemoteMeal> callback);
}
