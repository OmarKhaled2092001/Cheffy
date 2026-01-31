package com.example.cheffy.data.meals.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cheffy.data.meals.models.local.FavoriteMealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavoriteMealDao {

    @Query("SELECT * FROM favorite_meals ORDER BY added_at DESC")
    Flowable<List<FavoriteMealEntity>> observeAllFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteMealEntity meal);

    @Delete
    Completable deleteFavorite(FavoriteMealEntity meal);

    @Query("DELETE FROM favorite_meals WHERE id_meal = :mealId")
    Completable deleteFavoriteById(String mealId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE id_meal = :mealId)")
    Single<Boolean> isFavorite(String mealId);

    @Query("SELECT * FROM favorite_meals WHERE id_meal = :mealId")
    Single<FavoriteMealEntity> getFavoriteById(String mealId);
}
