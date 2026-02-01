package com.example.cheffy.data.meals.datasource.cloud;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.models.local.MealPlanEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface IMealsCloudDataSource {

    Completable uploadFavorite(String uid, FavoriteMealEntity meal);
    Completable deleteFavorite(String uid, String mealId);

    Completable uploadMealPlan(String uid, MealPlanEntity plan);
    Completable deleteMealPlan(String uid, String mealId, String dayOfWeek);


    Single<List<FavoriteMealEntity>> fetchAllFavorites(String uid);
    Single<List<MealPlanEntity>> fetchAllMealPlans(String uid);


    Completable uploadAllFavorites(String uid, List<FavoriteMealEntity> meals);
    Completable uploadAllMealPlans(String uid, List<MealPlanEntity> plans);


    Single<Boolean> hasCloudData(String uid);
}
