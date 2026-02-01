package com.example.cheffy.data.meals.datasource.local;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.models.local.MealPlanEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public interface IMealsLocalDataSource {
    Flowable<List<FavoriteMealEntity>> observeFavorites();

    Completable addFavorite(FavoriteMealEntity meal);

    Completable removeFavoriteById(String mealId);

    Single<Boolean> isFavorite(String mealId);

    Flowable<List<MealPlanEntity>> observeMealPlansByDay(String dayOfWeek);

    Completable addMealToPlan(MealPlanEntity mealPlan);

    Completable removeMealFromPlan(long planId);


    Single<List<FavoriteMealEntity>> getAllFavorites();

    Single<List<MealPlanEntity>> getAllMealPlans();

    Completable insertAllFavorites(List<FavoriteMealEntity> meals);

    Completable insertAllMealPlans(List<MealPlanEntity> plans);

    Completable clearAllFavorites();

    Completable clearAllMealPlans();
}
