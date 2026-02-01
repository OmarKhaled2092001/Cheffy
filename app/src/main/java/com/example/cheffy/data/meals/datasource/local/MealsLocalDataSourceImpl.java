package com.example.cheffy.data.meals.datasource.local;

import android.content.Context;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;
import com.example.cheffy.data.meals.models.local.MealPlanEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSourceImpl implements IMealsLocalDataSource {

    private final FavoriteMealDao favoriteMealDao;
    private final MealPlanDao mealPlanDao;
    private static MealsLocalDataSourceImpl instance;

    private MealsLocalDataSourceImpl(Context context) {
        MealsDatabase database = MealsDatabase.getInstance(context);
        this.favoriteMealDao = database.favoriteMealDao();
        this.mealPlanDao = database.mealPlanDao();
    }

    public static synchronized MealsLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealsLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public Flowable<List<FavoriteMealEntity>> observeFavorites() {
        return favoriteMealDao.observeAllFavorites();
    }

    @Override
    public Completable addFavorite(FavoriteMealEntity meal) {
        return favoriteMealDao.insertFavorite(meal);
    }

    @Override
    public Completable removeFavoriteById(String mealId) {
        return favoriteMealDao.deleteFavoriteById(mealId);
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return favoriteMealDao.isFavorite(mealId);
    }

    @Override
    public Flowable<List<MealPlanEntity>> observeMealPlansByDay(String dayOfWeek) {
        return mealPlanDao.observeMealsByDay(dayOfWeek);
    }

    @Override
    public Completable addMealToPlan(MealPlanEntity mealPlan) {
        return mealPlanDao.insertMealPlan(mealPlan);
    }

    @Override
    public Completable removeMealFromPlan(long planId) {
        return mealPlanDao.deleteMealPlanById(planId);
    }


    @Override
    public Single<List<FavoriteMealEntity>> getAllFavorites() {
        return favoriteMealDao.getAllFavorites();
    }

    @Override
    public Single<List<MealPlanEntity>> getAllMealPlans() {
        return mealPlanDao.getAllMealPlans();
    }

    @Override
    public Completable insertAllFavorites(List<FavoriteMealEntity> meals) {
        return favoriteMealDao.insertAllFavorites(meals);
    }

    @Override
    public Completable insertAllMealPlans(List<MealPlanEntity> plans) {
        return mealPlanDao.insertAllMealPlans(plans);
    }

    @Override
    public Completable clearAllFavorites() {
        return favoriteMealDao.deleteAllFavorites();
    }

    @Override
    public Completable clearAllMealPlans() {
        return mealPlanDao.deleteAllMealPlans();
    }
}
