package com.example.cheffy.data.meals.datasource.remote;

import com.example.cheffy.data.meals.models.Area;
import com.example.cheffy.data.meals.models.Category;
import com.example.cheffy.data.meals.models.Ingredient;
import com.example.cheffy.data.meals.models.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface IMealsRemoteDataSource {
    Single<RemoteMeal> getRandomMeal();

    Single<List<Category>> getCategories();

    Single<List<Area>> getAreas();

    Single<List<Ingredient>> getIngredients();

    Single<List<RemoteMeal>> getPopularMeals(int count);

    Single<List<RemoteMeal>> getMealsByFilter(SearchType type, String filter);

    Single<RemoteMeal> getMealById(String mealId);
}
