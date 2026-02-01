package com.example.cheffy.data.meals.repository;

import com.example.cheffy.data.meals.models.remote.Area;
import com.example.cheffy.data.meals.models.remote.Category;
import com.example.cheffy.data.meals.models.remote.Ingredient;
import com.example.cheffy.data.meals.models.remote.RemoteMeal;
import com.example.cheffy.data.meals.models.SearchType;
import com.example.cheffy.ui.plan.model.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IMealsRepository {
    Single<RemoteMeal> getRandomMeal();

    Single<List<Category>> getCategories();

    Single<List<Area>> getAreas();

    Single<List<Ingredient>> getIngredients();

    Single<List<RemoteMeal>> getPopularMeals(int count);

    Single<List<RemoteMeal>> getMealsByFilter(SearchType type, String filter);

    Single<List<RemoteMeal>> searchMealsByName(String query);

    Single<List<RemoteMeal>> searchMealsByFirstLetter(String letter);

    Single<RemoteMeal> getMealById(String mealId);

    Flowable<List<RemoteMeal>> observeFavorites();

    Completable addFavorite(RemoteMeal meal);

    Completable removeFavorite(RemoteMeal meal);

    Single<Boolean> isFavorite(String mealId);

    Flowable<List<PlannedMeal>> observeMealPlanByDay(String dayOfWeek);

    Completable addMealToPlan(RemoteMeal meal, String dayOfWeek);

    Completable removeMealFromPlanWithSync(String mealId, String dayOfWeek, long planId);

    Completable restoreDataFromCloud();

    Completable backupLocalDataToCloud();

    Single<Boolean> hasCloudData();

    Completable clearAllLocalData();
}
