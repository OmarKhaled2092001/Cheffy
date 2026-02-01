package com.example.cheffy.data.meals.datasource.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cheffy.data.meals.models.local.MealPlanEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealPlanDao {

    @Query("SELECT * FROM meal_plans WHERE day_of_week = :dayOfWeek ORDER BY added_at DESC")
    Flowable<List<MealPlanEntity>> observeMealsByDay(String dayOfWeek);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMealPlan(MealPlanEntity mealPlan);

    @Query("DELETE FROM meal_plans WHERE id = :planId")
    Completable deleteMealPlanById(long planId);

    @Query("SELECT * FROM meal_plans")
    Single<List<MealPlanEntity>> getAllMealPlans();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllMealPlans(List<MealPlanEntity> plans);

    @Query("DELETE FROM meal_plans")
    Completable deleteAllMealPlans();
}
